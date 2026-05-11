package co.quind.peajes.tollcollection.infrastructure.web;

import co.quind.peajes.tollcollection.application.command.RegisterManualOverrideCommand;
import co.quind.peajes.tollcollection.application.dto.ManualOverrideResponse;
import co.quind.peajes.tollcollection.application.dto.RegisterManualOverrideRequest;
import co.quind.peajes.tollcollection.domain.port.in.RegisterManualOverrideUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/stations/{stationId}/lanes/{laneId}/manual-overrides")
@RequiredArgsConstructor
public class ManualOverrideController {

	private final RegisterManualOverrideUseCase registerUseCase;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ManualOverrideResponse> registerManualOverride(
			@PathVariable String stationId,
			@PathVariable String laneId,
			@Valid @RequestBody RegisterManualOverrideRequest request,
			ServerWebExchange exchange) {
		var command = new RegisterManualOverrideCommand(
			stationId,
			laneId,
			request.licensePlate(),
			request.vehicleClass(),
			request.reason(),
			request.operatorId(),
			"",
			request.lprPhotoUrl()
		);
		return registerUseCase.register(command)
			.doOnSuccess(r -> log.info("Manual override registered: overrideId={}", r.overrideId()))
			.doOnError(ex -> log.error("Error registering manual override", ex));
	}

}
