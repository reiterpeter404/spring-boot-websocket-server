package com.websocket.server.event;

import com.websocket.server.controller.MessageController;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * The event listener for the websocket.
 */
@Component
@AllArgsConstructor
public class WebSocketEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final MessageController messageController;

    /**
     * Handle event of new websocket client connection.
     *
     * @param event the connection event
     */
    @EventListener
    public void handleWebSocketConnectionEstablished(final WebSocketConnectionEstablishedEvent event) {
        final WebSocketSession session = event.getSession();
        LOGGER.info("Reconnection event triggered.");
        messageController.sendMessagesFromMessageBufferToReconnectedSession(session);
    }
}
