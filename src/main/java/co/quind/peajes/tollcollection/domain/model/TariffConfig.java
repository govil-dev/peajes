package co.quind.peajes.tollcollection.domain.model;

import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;

/**
 * Tarifa vigente para una clase de vehículo en una estación. Definida por ANI.
 */
public record TariffConfig(
        StationId stationId,
        VehicleClass vehicleClass,
        MoneyAmount amount
) {}
