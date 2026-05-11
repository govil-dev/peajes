package co.quind.peajes.tollcollection.infrastructure.web;

import co.quind.peajes.tollcollection.application.command.ProcessTollPassCommand;
import co.quind.peajes.tollcollection.application.dto.TollPassRequest;
import co.quind.peajes.tollcollection.application.dto.TollPassResponse;
import co.quind.peajes.tollcollection.application.dto.TollPassResult;
import co.quind.peajes.tollcollection.domain.port.in.ProcessTollPassUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/toll-passes")
@RequiredArgsConstructor
public class TollPassController {

	private final ProcessTollPassUseCase processUseCase;

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public Mono<TollPassResponse> processTollPass(
			@Valid @RequestBody TollPassRequest request,
			ServerWebExchange exchange) {
		var command = new ProcessTollPassCommand(
			request.tagId(),
			request.stationId(),
			request.laneId(),
			request.vehicleClass(),
			request.detectedAt()
		);
		return processUseCase.process(command)
			.map(this::toResponse)
			.doOnSuccess(r -> log.info("Toll pass processed: status={}, passId={}", r.status(), r.passId()))
			.doOnError(ex -> log.error("Error processing toll pass", ex));
	}

	private TollPassResponse toResponse(TollPassResult result) {
		return new TollPassResponse(
			result.passId(),
			result.status().name(),
			result.declineReason() != null ? result.declineReason().name() : null,
			result.amount(),
			result.currency(),
			result.status().name().equals("AUTHORIZED") ? result.processedAt() : null,
			result.status().name().equals("DECLINED") ? result.processedAt() : null
		);
	}
}
