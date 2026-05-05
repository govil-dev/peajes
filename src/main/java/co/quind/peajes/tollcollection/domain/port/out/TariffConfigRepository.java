package co.quind.peajes.tollcollection.domain.port.out;

import co.quind.peajes.tollcollection.domain.model.TariffConfig;
import co.quind.peajes.tollcollection.domain.model.VehicleClass;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import reactor.core.publisher.Mono;

public interface TariffConfigRepository {
	Mono<TariffConfig> findActiveByStationAndClass(StationId stationId, VehicleClass vehicleClass);
}
