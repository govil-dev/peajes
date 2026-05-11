package co.quind.peajes.tollcollection.domain.port.in;

import co.quind.peajes.tollcollection.application.command.RegisterManualOverrideCommand;
import co.quind.peajes.tollcollection.application.dto.ManualOverrideResponse;
import reactor.core.publisher.Mono;

public interface RegisterManualOverrideUseCase {
	Mono<ManualOverrideResponse> register(RegisterManualOverrideCommand command);
}
