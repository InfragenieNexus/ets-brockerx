package com.log430.brockerx.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.log430.brockerx.dto.MarketUpdateDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MarketDataWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final ObjectMapper mapper = new ObjectMapper();

    @Override public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
    }

    // Simulate market updates every second
    @Scheduled(fixedRate = 1000) public void sendMarketUpdates() throws Exception {
        if (sessions.isEmpty())
            return;

        MarketUpdateDto update = new MarketUpdateDto("AAPL", 150 + Math.random() * 5,  // lastPrice
                                                     149,                       // open
                                                     152,                       // high
                                                     148,                       // low
                                                     10000,                     // volume
                                                     Instant.now());

        String payload = mapper.writeValueAsString(update);
        TextMessage message = new TextMessage(payload);

        for (WebSocketSession session : sessions) {
            if (session.isOpen())
                session.sendMessage(message);
        }
    }
}
