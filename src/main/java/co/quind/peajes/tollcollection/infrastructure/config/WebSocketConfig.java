package co.quind.peajes.tollcollection.infrastructure.config;

import co.quind.peajes.tollcollection.infrastructure.adapter.websocket.BarrierWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

@Configuration
public class WebSocketConfig {

    @Bean
    public HandlerMapping barrierWebSocketHandlerMapping(BarrierWebSocketHandler handler) {
        var map = Map.of("/ws/barriers", handler);
        var mapping = new SimpleUrlHandlerMapping(map);
        mapping.setOrder(-1);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
