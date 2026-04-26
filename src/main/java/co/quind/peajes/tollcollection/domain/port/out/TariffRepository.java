package co.quind.peajes.tollcollection.domain.port.out;

import co.quind.peajes.tollcollection.domain.model.TariffConfig;
import co.quind.peajes.tollcollection.domain.model.VehicleClass;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import reactor.core.publisher.Mono;

/** Puerto de salida para consulta de tarifas vigentes. */
public interface TariffRepository {
    Mono<TariffConfig> findActiveByStationAndVehicleClass(StationId stationId, VehicleClass vehicleClass);
}
