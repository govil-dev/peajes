package co.quind.peajes.tollcollection.infrastructure.persistence;

import co.quind.peajes.tollcollection.domain.model.TariffConfig;
import co.quind.peajes.tollcollection.domain.model.VehicleClass;
import co.quind.peajes.tollcollection.domain.port.out.TariffConfigRepository;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TariffConfigRepositoryAdapter implements TariffConfigRepository {

	private final TariffConfigR2dbcRepository r2dbcRepository;

	@Override
	public Mono<TariffConfig> findActiveByStationAndClass(StationId stationId, VehicleClass vehicleClass) {
		return r2dbcRepository.findActiveByStationAndClass(
				stationId.value(),
				vehicleClass.name(),
				LocalDate.now()
			)
			.map(this::toDomain);
	}

	private TariffConfig toDomain(TariffConfigEntity entity) {
		return new TariffConfig(
			entity.id(),
			new StationId(entity.stationId()),
			VehicleClass.valueOf(entity.vehicleClass()),
			new MoneyAmount(entity.amount(), entity.currency()),
			entity.validFrom(),
			entity.validUntil(),
			entity.active()
		);
	}

}
