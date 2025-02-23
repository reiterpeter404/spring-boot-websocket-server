package com.websocket.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class WebSocketConnectionEstablishedEvent extends ApplicationEvent {
    private final transient WebSocketSession session;

    public WebSocketConnectionEstablishedEvent(final Object source, final WebSocketSession session) {
        super(source);
        this.session = session;
    }
}
