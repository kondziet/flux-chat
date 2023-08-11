package pl.kondziet.springbackend.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import pl.kondziet.springbackend.websocket.handler.DirectMessageHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketServerConfig {
    @Bean
    public HandlerMapping webSocketMapping(DirectMessageHandler directMessageHandler) {
        Map<String, WebSocketHandler> handlersMap = new HashMap<>();
        handlersMap.put("/chat/{chatRoomId}", directMessageHandler);

        return new SimpleUrlHandlerMapping(handlersMap, -1);
    }
}
