package co.quind.peajes.tollcollection.application.usecase;

import co.quind.peajes.tollcollection.application.dto.TollPassRequest;
import co.quind.peajes.tollcollection.application.dto.TollPassResponse;
import co.quind.peajes.tollcollection.domain.exception.InsufficientBalanceException;
import co.quind.peajes.tollcollection.domain.model.DeclineReason;
import co.quind.peajes.tollcollection.domain.model.TollPass;
import co.quind.peajes.tollcollection.domain.model.VehicleClass;
import co.quind.peajes.tollcollection.domain.port.in.ProcessTollPassUseCase;
import co.quind.peajes.tollcollection.domain.port.out.*;
import co.quind.peajes.tollcollection.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessTollPassService implements ProcessTollPassUseCase {

	private final TollPassRepository tollPassRepository;
	private final LaneRepository laneRepository;
	private final TariffConfigRepository tariffConfigRepository;
	private final AccountManagementPort accountManagementPort;
	private final DomainEventPublisher eventPublisher;

	@Override
	public Mono<TollPassResponse> process(TollPassRequest request) {
		PassId passId = PassId.of(TagId.of(request.tagId()), request.detectedAt());
		TagId tagId = TagId.of(request.tagId());
		StationId stationId = StationId.of(request.stationId());
		LaneId laneId = LaneId.of(request.laneId());

		log.info("Processing toll pass: passId={}, tagId={}", passId, tagId.toMasked());

		// Idempotency check
		return tollPassRepository.findByPassId(passId)
			.map(existing -> {
				log.info("TollPass already processed (idempotent): passId={}", passId);
				return TollPassResponse.fromExisting(existing);
			})
			.switchIfEmpty(processNewPass(passId, tagId, stationId, laneId, request));
	}

	private boolean isLaneBlocked(co.quind.peajes.tollcollection.domain.model.Lane lane) {
		return !lane.isOpen();
	}

	private Mono<TollPassResponse> processNewPass(PassId passId, TagId tagId, StationId stationId,
												  LaneId laneId, TollPassRequest request) {
		VehicleClass vehicleClass = VehicleClass.valueOf(request.vehicleClass());

		// Parallel lookups: lane + tariff
		return Mono.zip(
				laneRepository.findByLaneId(laneId)
					.doOnNext(lane -> log.debug("Lane loaded: laneId={}, status={}", laneId, lane.status())),
				tariffConfigRepository.findActiveByStationAndClass(stationId, vehicleClass)
					.doOnNext(tariff -> log.debug("Tariff loaded: station={}, class={}, amount={}",
						stationId, vehicleClass, tariff.amount().toDisplayString()))
			)
			.flatMap(tuple -> {
				var lane = tuple.getT1();
				var tariffConfig = tuple.getT2();

				// Validate lane is open
				if (isLaneBlocked(lane)) {
					log.warn("Lane not open: laneId={}, status={}", laneId, lane.status());
					TollPass declined = TollPass.decline(passId, tagId, stationId, laneId,
						DeclineReason.LANE_NOT_OPEN, request.detectedAt());
					return saveAndPublish(declined);
				}

				// Deduct balance with CB + timeout
				return accountManagementPort.deductBalance(tagId, tariffConfig.amount(), passId)
					.flatMap(snapshot -> {
						log.info("Balance deducted successfully: tagId={}, balanceAfter={}",
							tagId.toMasked(), snapshot.balance().toDisplayString());
						TollPass authorized = TollPass.authorize(passId, tagId, stationId, laneId,
							vehicleClass, tariffConfig.amount(), request.detectedAt());
						return saveAndPublish(authorized);
					})
					.onErrorResume(InsufficientBalanceException.class, ex -> {
						log.warn("Insufficient balance: tagId={}, message={}",
							tagId.toMasked(), ex.getMessage());
						TollPass declined = TollPass.decline(passId, tagId, stationId, laneId,
							DeclineReason.INSUFFICIENT_BALANCE, request.detectedAt());
						return saveAndPublish(declined);
					})
					.onErrorResume(Exception.class, ex -> {
						log.error("System error processing toll pass: passId={}, tagId={}, error={}",
							passId, tagId.toMasked(), ex.getMessage(), ex);
						TollPass declined = TollPass.decline(passId, tagId, stationId, laneId,
							DeclineReason.SYSTEM_ERROR, request.detectedAt());
						return saveAndPublish(declined);
					});
			});
	}

	private Mono<TollPassResponse> saveAndPublish(TollPass tollPass) {
		var events = tollPass.pullDomainEvents();
		return tollPassRepository.save(tollPass)
			.doOnNext(saved -> log.info("TollPass saved: passId={}, status={}", saved.passId(), saved.status()))
			.flatMap(saved ->
				eventPublisher.publishAll(events)
					.then(Mono.just(saved))
					.doOnSuccess(s -> log.info("Domain events published: count={}", events.size()))
			)
			.map(TollPassResponse::from)
			.doOnError(ex -> log.error("Error saving/publishing toll pass", ex));
	}

}
