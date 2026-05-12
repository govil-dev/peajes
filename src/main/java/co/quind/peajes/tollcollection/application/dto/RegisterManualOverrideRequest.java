package co.quind.peajes.tollcollection.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterManualOverrideRequest(
	@JsonProperty("licensePlate")
	String licensePlate,

	@JsonProperty("vehicleClass")
	String vehicleClass,

	@JsonProperty("reason")
	String reason,

	@JsonProperty("operatorId")
	String operatorId,

	@JsonProperty("lprPhotoUrl")
	String lprPhotoUrl
) {}
