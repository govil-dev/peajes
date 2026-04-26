package co.quind.peajes.tollcollection.infrastructure.persistence;

import co.quind.peajes.tollcollection.domain.model.TariffConfig;
import co.quind.peajes.tollcollection.domain.model.VehicleClass;
import co.quind.peajes.tollcollection.domain.port.out.TariffRepository;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TariffPersistenceAdapter implements TariffRepository {

    private final TariffConfigR2dbcRepository r2dbcRepository;

    public TariffPersistenceAdapter(TariffConfigR2dbcRepository r2dbcRepository) {
        this.r2dbcRepository = r2dbcRepository;
    }

    @Override
    public Mono<TariffConfig> findActiveByStationAndVehicleClass(StationId stationId, VehicleClass vehicleClass) {
        return r2dbcRepository
                .findByStationIdAndVehicleClassAndActiveTrue(stationId.value(), vehicleClass.name())
                .map(e -> new TariffConfig(stationId, vehicleClass,
                        new MoneyAmount(e.getAmount(), e.getCurrency())));
    }
}
