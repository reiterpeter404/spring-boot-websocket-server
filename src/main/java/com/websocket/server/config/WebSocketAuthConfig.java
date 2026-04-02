package com.websocket.server.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * Load the configuration for authentication from the properties file.
 */
@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "websocket.authentication")
public class WebSocketAuthConfig {
    private Boolean enableLoginWithCredentials = false;
    private String username;
    private String password;

    private Boolean enableLoginWithToken = false;
    private String jwtSecret;

    public boolean checkCredentials(final String username, final String password) {
        boolean result = Objects.equals(this.username, username)
                && Objects.equals(this.password, password);
        if (!result) {
            log.error("Authentication failed: Invalid credentials");
        }
        return result;
    }

    public boolean checkJwtToken(final String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            log.error("Failed to parse or invalid JWT token.");
            return false;
        }
    }
}
