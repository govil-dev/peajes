package co.quind.peajes.tollcollection.domain.port.in;

import co.quind.peajes.tollcollection.application.dto.TollPassRequest;
import co.quind.peajes.tollcollection.application.dto.TollPassResponse;
import reactor.core.publisher.Mono;

public interface ProcessTollPassUseCase {
	Mono<TollPassResponse> process(TollPassRequest request);
}
