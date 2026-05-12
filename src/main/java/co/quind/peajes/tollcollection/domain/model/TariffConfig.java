package co.quind.peajes.tollcollection.domain.model;

import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import java.time.LocalDate;
import java.util.UUID;

public record TariffConfig(UUID id, StationId stationId, VehicleClass vehicleClass,
							MoneyAmount amount, LocalDate validFrom, LocalDate validUntil,
							boolean active) {

	public boolean isValidOn(LocalDate date) {
		if (!active) {
			return false;
		}
		if (date.isBefore(validFrom)) {
			return false;
		}
		if (validUntil != null && date.isAfter(validUntil)) {
			return false;
		}
		return true;
	}

}
