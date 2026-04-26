package co.quind.peajes.tollcollection.infrastructure.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Propaga correlationId y traceId al MDC para trazabilidad por request.
 * El tagId y stationId se agregan al MDC en el caso de uso.
 */
@Component
public class MdcWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String correlationId = exchange.getRequest().getHeaders()
                .getFirst("X-Correlation-Id");
        if (correlationId == null) correlationId = UUID.randomUUID().toString();
        String traceId = exchange.getRequest().getHeaders()
                .getFirst("X-Trace-Id");
        if (traceId == null) traceId = UUID.randomUUID().toString();

        MDC.put("correlationId", correlationId);
        MDC.put("traceId", traceId);

        return chain.filter(exchange)
                .doFinally(signal -> MDC.clear());
    }
}
