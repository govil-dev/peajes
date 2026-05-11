package co.quind.peajes.tollcollection.application.command;

public record RegisterManualOverrideCommand(
	String stationId,
	String laneId,
	String licensePlate,
	String vehicleClass,
	String reason,
	String operatorId,
	String amount,
	String lprPhotoUrl
) {}
