package com.websocket.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.server.model.WebSocketDto;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TradeWebSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeWebSocketHandler.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final List<WebSocketSession> sessionList = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        LOGGER.info("Session established: {}", session.getId());
        sessionList.add(session);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, @NonNull final CloseStatus status) {
        LOGGER.info("Session closed:      {}", session.getId());
        sessionList.remove(session);
    }

    @Override
    public void handleTextMessage(final WebSocketSession session , final TextMessage message) throws IOException {
        final WebSocketDto webSocketDto = OBJECT_MAPPER.readValue(message.getPayload(), WebSocketDto.class);
        LOGGER.info("Received message from session {}: {}", session.getId(), webSocketDto);
    }

    public List<String> getSessionIDs() {
        final List<String> sessionIDs = new ArrayList<>();
        for (WebSocketSession webSocketSession : sessionList) {
            sessionIDs.add(webSocketSession.getId());
        }
        return sessionIDs;
    }

    public void sendMessageToClientId(final String clientId, final WebSocketDto messageDto) throws IOException {
        final WebSocketSession session = sessionList.stream().filter(wsSession -> wsSession.getId().equals(clientId)).findFirst().orElseThrow(NullPointerException::new);
        sendMessageToSession(session, messageDto);
    }

    public void broadcastMessage(final WebSocketDto messageDto) throws IOException {
        final String message = OBJECT_MAPPER.writeValueAsString(messageDto);
        for (WebSocketSession webSocketSession : sessionList) {
            sendDtoAsMessage(webSocketSession, message);
        }
    }

    private void sendMessageToSession(final WebSocketSession session, final WebSocketDto messageDto) throws IOException {
        final String message = OBJECT_MAPPER.writeValueAsString(messageDto);
        sendDtoAsMessage(session, message);
    }

    private void sendDtoAsMessage(final WebSocketSession session, final String message) throws IOException {
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }
}
