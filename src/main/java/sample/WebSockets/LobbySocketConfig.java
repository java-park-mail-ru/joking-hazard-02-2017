package sample.WebSockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import sample.Game.WebSockets.GameWebSocketHandler;
import sample.Lobby.WebSockets.LobbyWebSocketHandler;

/**
 * Created by ksg on 11.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
@Configuration
@EnableWebSocket
public class LobbySocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(LobbyWebSocketHandler(), "/lobby")
                .setAllowedOrigins("https://jokinghazard.herokuapp.com")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
        registry.addHandler(GameWebSocketHandler(), "/game")
                .setAllowedOrigins("https://jokinghazard.herokuapp.com")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }

    @Bean
    public WebSocketHandler LobbyWebSocketHandler() {
        return new LobbyWebSocketHandler();
    }

    @Bean
    public WebSocketHandler GameWebSocketHandler() {
        return new GameWebSocketHandler();
    }
}
