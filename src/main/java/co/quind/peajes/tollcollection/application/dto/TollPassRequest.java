package co.quind.peajes.tollcollection.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record TollPassRequest(
	@NotBlank(message = "tagId is required")
	@Size(min = 8, max = 8, message = "tagId must be 8 characters")
	String tagId,

	@NotBlank(message = "stationId is required")
	String stationId,

	@NotBlank(message = "laneId is required")
	String laneId,

	@NotNull(message = "vehicleClass is required")
	String vehicleClass,

	@NotNull(message = "detectedAt is required")
	Instant detectedAt
) {}
