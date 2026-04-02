package com.websocket.server.interceptor;

import com.websocket.server.config.WebSocketBasicAuthConfig;
import com.websocket.server.config.WebSocketTokenAuthConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Base64;
import java.util.Map;

/**
 * The handshake interceptor to establish a connection.
 */
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketAuthInterceptor.class);
    private static final String BASIC = "Basic ";
    private static final String BEARER = "Bearer ";

    private final WebSocketBasicAuthConfig webSocketBasicAuthConfig;
    private final WebSocketTokenAuthConfig webSocketTokenAuthConfig;

    /**
     * Invoked before the handshake is processed.
     *
     * @param request    the current request
     * @param response   the current response
     * @param wsHandler  the target WebSocket handler
     * @param attributes the attributes from the HTTP handshake to associate with the WebSocket
     *                   session; the provided attributes are copied, the original map is not used.
     * @return whether to proceed with the handshake ({@code true}) or abort ({@code false})
     */
    @Override
    public boolean beforeHandshake(
            final ServerHttpRequest request,
            @NonNull final ServerHttpResponse response,
            @NonNull final WebSocketHandler wsHandler,
            @NonNull final Map<String, Object> attributes) {
        final String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            return false;
        }
        if (authHeader.startsWith(BEARER)) {
            return validateJwtToken(authHeader);
        }
        if (authHeader.startsWith(BASIC)) {
            return validateAuthenticationCredentials(authHeader);
        }

        LOGGER.error("Failed to authenticate client application.");
        return false;
    }

    /**
     * Invoked after the handshake is done. The response status and headers indicate
     * the results of the handshake, i.e. whether it was successful or not.
     *
     * @param request   the current request
     * @param response  the current response
     * @param wsHandler the target WebSocket handler
     * @param exception an exception raised during the handshake, or {@code null} if none
     */
    @Override
    public void afterHandshake(
            @NonNull final ServerHttpRequest request,
            @NonNull final ServerHttpResponse response,
            @NonNull final WebSocketHandler wsHandler,
            final Exception exception
    ) {
        // not needed for this example
    }

    private boolean validateJwtToken(final String authHeader) {
        if (!webSocketTokenAuthConfig.getEnableLoginWithToken()) {
            return false;
        }

        final String token = authHeader.substring(BEARER.length());
        return webSocketTokenAuthConfig.verify(token);
    }

    private boolean validateAuthenticationCredentials(final String authHeader) {
        if (!webSocketBasicAuthConfig.getEnableLoginWithCredentials()) {
            return false;
        }
        final String[] values = decodeAuthenticationCredentials(authHeader);
        return webSocketBasicAuthConfig.verify(values[0], values[1]);
    }

    private String[] decodeAuthenticationCredentials(final String authHeader) {
        final String base64Credentials = authHeader.substring(BASIC.length());
        final String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        return credentials.split(":", 2);
    }
}
