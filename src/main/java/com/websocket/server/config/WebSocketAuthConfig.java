package com.websocket.server.config;

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
    private Boolean credentials;
    private String username;
    private String password;

    public boolean checkCredentials(final String username, final String password) {
        if (!credentials) {
            log.warn("Service tried authorize with user credentials, but it is disabled in the application properties!");
            return false;
        }
        return Objects.equals(this.username, username)
                && Objects.equals(this.password, password);
    }
}
