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

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
    private final TradeWebSocketHandler tradeWebSocketHandler;

    public MessageController(final TradeWebSocketHandler tradeWebSocketHandler) {
        this.tradeWebSocketHandler = tradeWebSocketHandler;
    }

    @GetMapping("/")
    public List<String> getClientConnections() {
        return tradeWebSocketHandler.getSessionIDs();
    }

    @PostMapping("/send/{sessionId}")
    public void sendMessageToSessionID(@PathVariable String sessionId, @RequestBody WebSocketDto messageDto) {
        try {
            tradeWebSocketHandler.sendMessageToClientId(sessionId, messageDto);
        } catch (IOException exception) {
            LOGGER.error("Failed to send message to client with ID = {}", sessionId);
        }
    }

    @PostMapping("/send")
    public void broadcastMessageToAllSessions(@RequestBody WebSocketDto messageDto) {
        try {
            tradeWebSocketHandler.broadcastMessage(messageDto);
        } catch (IOException exception) {
            LOGGER.error("Failed to broadcast message.");
        }
    }
}
