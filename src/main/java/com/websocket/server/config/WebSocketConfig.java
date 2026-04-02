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

/**
 * The configuration class for the websocket.
 */
@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketBasicAuthConfig webSocketBasicAuthConfig;
    private final WebSocketTokenAuthConfig webSocketTokenAuthConfig;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Register the websocket configurations to the websocket handler.
     *
     * @param registry the registry to add the configurations
     */
    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry
                .addHandler(tradeWebSocketHandler(), "/websocket")
                .addInterceptors(new WebSocketAuthInterceptor(webSocketBasicAuthConfig, webSocketTokenAuthConfig));
    }

    @Bean
    public TradeWebSocketHandler tradeWebSocketHandler() {
        return new TradeWebSocketHandler(eventPublisher);
    }
}
