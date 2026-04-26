package co.quind.peajes.tollcollection.infrastructure.adapter.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * Maneja conexiones WebSocket entrantes de barreras físicas.
 * La barrera se identifica por el query param laneId en la URL de conexión.
 */
@Component
public class BarrierWebSocketHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(BarrierWebSocketHandler.class);

    private final BarrierSessionRegistry sessionRegistry;

    public BarrierWebSocketHandler(BarrierSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String laneId = extractLaneId(session);
        sessionRegistry.register(laneId, session);
        log.info("Barrera conectada: laneId={}", laneId);
        return session.receive()
                .doOnTerminate(() -> {
                    sessionRegistry.deregister(laneId);
                    log.info("Barrera desconectada: laneId={}", laneId);
                })
                .then();
    }

    private String extractLaneId(WebSocketSession session) {
        var query = session.getHandshakeInfo().getUri().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("laneId=")) return param.substring(7);
            }
        }
        return session.getId();
    }
}
