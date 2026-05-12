package co.quind.peajes.tollcollection.infrastructure.persistence;

import co.quind.peajes.tollcollection.domain.model.TariffConfig;
import co.quind.peajes.tollcollection.domain.model.VehicleClass;
import co.quind.peajes.tollcollection.domain.port.out.TariffRepository;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TariffPersistenceAdapter implements TariffRepository {

    private final TariffConfigR2dbcRepository r2dbcRepository;

    @Override
    public Mono<TariffConfig> findActiveByStationAndVehicleClass(StationId stationId, VehicleClass vehicleClass) {
        return r2dbcRepository
                .findActiveByStationAndClass(stationId.value(), vehicleClass.name(), LocalDate.now())
                .map(e -> new TariffConfig(
                        e.id(),
                        new StationId(e.stationId()),
                        VehicleClass.valueOf(e.vehicleClass()),
                        new MoneyAmount(e.amount(), e.currency()),
                        e.validFrom(),
                        e.validUntil(),
                        e.active()
                ));
    }
}
