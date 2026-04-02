package com.websocket.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * The DTO to send and receive messages.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketDto {
    private UUID uuid;
    private String message;

    /**
     * Convert the DTO into a string.
     *
     * @return the DTO as string
     */
    @Override
    public String toString() {
        return "WebSocketDto[message = " + message + "]";
    }
}
