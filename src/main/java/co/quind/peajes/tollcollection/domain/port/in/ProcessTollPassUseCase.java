package co.quind.peajes.tollcollection.domain.port.in;

import co.quind.peajes.tollcollection.application.command.ProcessTollPassCommand;
import co.quind.peajes.tollcollection.application.dto.TollPassResult;
import reactor.core.publisher.Mono;

public interface ProcessTollPassUseCase {
	Mono<TollPassResult> process(ProcessTollPassCommand command);
}
