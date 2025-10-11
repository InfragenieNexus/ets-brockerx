package com.log430.brockerx.config;

import com.log430.brockerx.websocket.MarketDataWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MarketDataWebSocketHandler marketDataHandler;

    public WebSocketConfig(MarketDataWebSocketHandler marketDataHandler) {
        this.marketDataHandler = marketDataHandler;
    }

    @Override public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(marketDataHandler, "/ws/market").setAllowedOrigins("*");
    }
}
