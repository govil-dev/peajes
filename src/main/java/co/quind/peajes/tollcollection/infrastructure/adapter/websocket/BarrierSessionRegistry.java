package co.quind.peajes.tollcollection.infrastructure.adapter.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Registro de sesiones WebSocket activas de barreras físicas por laneId.
 */
@Component
public class BarrierSessionRegistry {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void register(String laneId, WebSocketSession session) {
        sessions.put(laneId, session);
    }

    public void deregister(String laneId) {
        sessions.remove(laneId);
    }

    public Mono<WebSocketSession> getSession(String laneId) {
        var session = sessions.get(laneId);
        return session != null ? Mono.just(session) : Mono.empty();
    }
}
