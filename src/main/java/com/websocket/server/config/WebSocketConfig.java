package com.websocket.server.config;

import com.websocket.server.handler.TradeWebSocketHandler;
import com.websocket.server.interceptor.WebSocketAuthInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketAuthConfig webSocketAuthConfig;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry
                .addHandler(tradeWebSocketHandler(), "/websocket")
                .addInterceptors(new WebSocketAuthInterceptor(webSocketAuthConfig));
    }

    @Bean
    public TradeWebSocketHandler tradeWebSocketHandler() {
        return new TradeWebSocketHandler(eventPublisher);
    }
}
