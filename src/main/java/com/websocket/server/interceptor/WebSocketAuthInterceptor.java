package com.websocket.server.interceptor;

import com.websocket.server.config.WebSocketAuthConfig;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Base64;
import java.util.Map;

@AllArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketAuthInterceptor.class);
    private final WebSocketAuthConfig webSocketAuthConfig;

    @Override
    public boolean beforeHandshake(
            final ServerHttpRequest request,
            @NonNull final ServerHttpResponse response,
            @NonNull final WebSocketHandler wsHandler,
            @NonNull final Map<String, Object> attributes) {
        final String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (validateAuthenticationCredentials(authHeader)) {
            return true;
        }
        LOGGER.error("Failed to authenticate client application.");
        return false;
    }

    private boolean validateAuthenticationCredentials(final String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return false;
        }
        final String[] values = decodeAuthenticationCredentials(authHeader);
        return webSocketAuthConfig.checkCredentials(values[0], values[1]);
    }

    private String[] decodeAuthenticationCredentials(final String authHeader) {
        final String base64Credentials = authHeader.substring(6);
        final String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        return credentials.split(":", 2);
    }

    @Override
    public void afterHandshake(
            @NonNull final ServerHttpRequest request,
            @NonNull final ServerHttpResponse response,
            @NonNull final WebSocketHandler wsHandler,
            final Exception exception
    ) {
        // not needed for this example
    }
}
