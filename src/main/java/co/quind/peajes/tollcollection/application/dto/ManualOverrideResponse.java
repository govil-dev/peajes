package co.quind.peajes.tollcollection.application.dto;

import co.quind.peajes.tollcollection.domain.model.ManualOverride;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ManualOverrideResponse(
	@JsonProperty("overrideId")
	String overrideId,

	@JsonProperty("stationId")
	String stationId,

	@JsonProperty("laneId")
	String laneId,

	@JsonProperty("licensePlate")
	String licensePlate,

	@JsonProperty("vehicleClass")
	String vehicleClass,

	@JsonProperty("reason")
	String reason,

	@JsonProperty("operatorId")
	String operatorId,

	@JsonProperty("amount")
	String amount,

	@JsonProperty("currency")
	String currency,

	@JsonProperty("registeredAt")
	String registeredAt,

	@JsonProperty("requiresAdminApproval")
	boolean requiresAdminApproval
) {

	public static ManualOverrideResponse fromManualOverride(ManualOverride override) {
		return new ManualOverrideResponse(
			override.overrideId().value(),
			override.stationId().toString(),
			override.laneId().toString(),
			override.licensePlate(),
			override.vehicleClass().name(),
			override.reason().name(),
			override.operatorId(),
			override.amount().toJsonString(),
			override.amount().currency(),
			override.registeredAt().toString(),
			override.requiresAdminApproval()
		);
	}

}
