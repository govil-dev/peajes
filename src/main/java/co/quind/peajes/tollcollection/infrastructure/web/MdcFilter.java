package co.quind.peajes.tollcollection.infrastructure.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MdcFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		String correlationId = Optional.ofNullable(
			exchange.getRequest().getHeaders().getFirst("X-Correlation-Id"))
			.orElse(UUID.randomUUID().toString());

		String traceId = Optional.ofNullable(
			exchange.getRequest().getHeaders().getFirst("X-Trace-Id"))
			.orElse(correlationId);

		return chain.filter(exchange)
			.contextWrite(reactor.util.context.Context.of(
				"correlationId", correlationId,
				"traceId", traceId
			))
			.doOnSubscribe(s -> {
				MDC.put("correlationId", correlationId);
				MDC.put("traceId", traceId);
			})
			.doFinally(signal -> MDC.clear());
	}

}
