package co.quind.peajes.tollcollection.infrastructure.web;

import co.quind.peajes.tollcollection.application.dto.TollPassRequest;
import co.quind.peajes.tollcollection.application.dto.TollPassResponse;
import co.quind.peajes.tollcollection.domain.port.in.ProcessTollPassUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/toll-passes")
@RequiredArgsConstructor
public class TollPassController {

	private final ProcessTollPassUseCase processUseCase;

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public Mono<TollPassResponse> processTollPass(
			@Valid @RequestBody TollPassRequest request,
			ServerWebExchange exchange) {
		return processUseCase.process(request)
			.doOnSuccess(response -> log.info("Toll pass processed: status={}, passId={}",
				response.status(), response.passId()))
			.doOnError(ex -> log.error("Error processing toll pass", ex));
	}

}
