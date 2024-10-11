package com.websocket.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * Load the configuration for authentication from the properties file.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "websocket.authentication")
public class WebSocketAuthConfig {
    private String username;
    private String password;

    public boolean checkCredentials(final String username, final String password) {
        return Objects.equals(this.username, username)
                && Objects.equals(this.password, password);
    }
}
