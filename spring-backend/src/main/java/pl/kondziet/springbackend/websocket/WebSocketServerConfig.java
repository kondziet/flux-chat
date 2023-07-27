package pl.kondziet.springbackend.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import pl.kondziet.springbackend.websocket.handler.ChatWebSocketHandler;

import java.util.Map;

@Configuration
public class WebSocketServerConfig {
    @Bean
    public HandlerMapping webSocketMapping(ChatWebSocketHandler webSocketHandler) {
        return new SimpleUrlHandlerMapping(Map.of("/chat", webSocketHandler), -1);
    }

    @Bean
    public void errorDroppedHandler() {
//        Hooks.onErrorDropped(error -> log.warn("Exception happened: ", error));
    }
}
