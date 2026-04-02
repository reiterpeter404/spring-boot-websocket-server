package com.websocket.server.controller;

import com.websocket.server.handler.TradeWebSocketHandler;
import com.websocket.server.model.WebSocketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The controller to get and send messages.
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
    private final TradeWebSocketHandler tradeWebSocketHandler;
    private final List<WebSocketDto> messageBuffer = new ArrayList<>();

    public MessageController(final TradeWebSocketHandler tradeWebSocketHandler) {
        this.tradeWebSocketHandler = tradeWebSocketHandler;
    }

    /**
     * Get all session IDs that are currently connected to the websocket server
     *
     * @return a list of all session IDs
     */
    @GetMapping("/")
    public List<String> getClientConnections() {
        return tradeWebSocketHandler.getSessionIDs();
    }

    /**
     * Send a message to a given session.
     *
     * @param sessionId  the session ID to send the message to
     * @param messageDto the message to send
     */
    @PostMapping("/send/{sessionId}")
    public void sendMessageToSessionID(@PathVariable String sessionId, @RequestBody WebSocketDto messageDto) {
        try {
            tradeWebSocketHandler.sendMessageToClientId(sessionId, messageDto);
        } catch (IOException exception) {
            messageBuffer.add(messageDto);
            LOGGER.error("Failed to send message to client with ID = {}", sessionId);
        }
    }

    /**
     * Broadcast a message to all connected sessions.
     *
     * @param messageDto the message to send
     */
    @PostMapping("/send")
    public void broadcastMessageToAllSessions(@RequestBody WebSocketDto messageDto) {
        try {
            tradeWebSocketHandler.broadcastMessage(messageDto);
        } catch (IOException exception) {
            messageBuffer.add(messageDto);
            LOGGER.error("Failed to broadcast message.");
        }
    }

    /**
     * Send all messages from the message buffer to the given session.
     *
     * @param session the session to send the messages to
     */
    public void sendMessagesFromMessageBufferToReconnectedSession(final WebSocketSession session) {
        try {
            for (WebSocketDto messageDto : messageBuffer) {
                tradeWebSocketHandler.sendMessageToSession(session, messageDto);
            }
        } catch (IOException exception) {
            LOGGER.error("Failed to load messages from message buffer to reconnected session.");
        }
    }
}
