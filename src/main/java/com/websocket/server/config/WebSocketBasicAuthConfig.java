package com.websocket.server.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "websocket.authentication.basic")
public class WebSocketBasicAuthConfig {
    private Boolean enableLoginWithCredentials = false;
    private String username;
    private String password;

    /**
     * Verify the user credentials.
     *
     * @param username the username to authenticate
     * @param password the password to authenticate
     * @return true, if the user credentials are correct. False otherwise.
     */
    public boolean verify(final String username, final String password) {
        boolean result = Objects.equals(this.username, username)
                && Objects.equals(this.password, password);
        if (!result) {
            log.error("Authentication failed: Invalid credentials");
        }
        return result;
    }
}
