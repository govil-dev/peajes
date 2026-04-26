package co.quind.peajes.tollcollection.infrastructure.adapter.websocket;

import co.quind.peajes.tollcollection.domain.port.out.BarrierControlPort;
import co.quind.peajes.tollcollection.domain.valueobject.LaneId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/** Implementa el control de barreras enviando comandos vía WebSocket al equipo físico. */
@Component
public class BarrierWebSocketAdapter implements BarrierControlPort {

    private static final Logger log = LoggerFactory.getLogger(BarrierWebSocketAdapter.class);

    private final BarrierSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    public BarrierWebSocketAdapter(BarrierSessionRegistry sessionRegistry, ObjectMapper objectMapper) {
        this.sessionRegistry = sessionRegistry;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> openBarrier(LaneId laneId) {
        return sendCommand(laneId, Map.of("command", "OPEN", "laneId", laneId.value()));
    }

    @Override
    public Mono<Void> notifyOperator(LaneId laneId, String message) {
        return sendCommand(laneId, Map.of("command", "NOTIFY", "laneId", laneId.value(), "message", message));
    }

    private Mono<Void> sendCommand(LaneId laneId, Map<String, String> payload) {
        return sessionRegistry.getSession(laneId.value())
                .flatMap(session -> {
                    try {
                        String json = objectMapper.writeValueAsString(payload);
                        return session.send(Mono.just(session.textMessage(json)));
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .doOnSuccess(v -> log.info("Comando enviado a barrera laneId={} payload={}", laneId.value(), payload))
                .doOnError(e -> log.warn("Barrera no conectada o error al enviar a laneId={}: {}",
                        laneId.value(), e.getMessage()))
                .onErrorResume(e -> Mono.empty());
    }
}
