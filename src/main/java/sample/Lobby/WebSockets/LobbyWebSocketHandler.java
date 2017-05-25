package sample.Lobby.WebSockets;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sample.Lobby.Commands.Command;
import sample.Lobby.Messages.ErrorMessage;
import sample.Lobby.Messages.OkMessage;
import sample.Lobby.Services.LobbyService;

import javax.naming.AuthenticationException;
import java.io.IOException;

/**
 * Created by ksg on 11.04.17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class LobbyWebSocketHandler extends TextWebSocketHandler {
    @SuppressWarnings({"SpringJavaAutowiredMembersInspection", "SpringAutowiredFieldsWarningInspection"})
    @Autowired
    private
    LobbyService lobbyService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        LOGGER.debug("connection establisher handler");
        lobbyService.addUser(webSocketSession);
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

        final String userId = (String) webSocketSession.getAttributes().get("userLogin");
        final Boolean rejected = (Boolean) webSocketSession.getAttributes().get("rejected");
        if ((userId != null) && (rejected == null)) {
            lobbyService.removeUser(userId);
        }
        webSocketSession.close();
        //TODO: sercer closed connection status?
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        final String userId = (String) session.getAttributes().get("userLogin");
        if (userId == null) {
            final ErrorMessage msg = new ErrorMessage("Invalid user session", objectMapper);
            @SuppressWarnings("LocalVariableNamingConvention") final String text_msg = msg.getJson();
            try {
                session.sendMessage(new TextMessage(text_msg));
            } catch (IOException ignored) {

            }
        } else {
            handleMessage(userId, session, message);
        }
    }

    private void handleMessage(String userId, WebSocketSession session, TextMessage text) {

        final Command cmd;
        try {
            cmd = objectMapper.readValue(text.getPayload(), Command.class);
        } catch (IOException ex) {
            LOGGER.error("wrong json format at first level", ex);
            final ErrorMessage msg = new ErrorMessage("Invalid first level json format", objectMapper);
            try {
                session.sendMessage(new TextMessage(msg.getJson()));
            } catch (IOException ignored) {
            }
            return;
        }
        switch (cmd.getCmd()) {
            case "EndSession": {
                endSession(userId, session);
                break;
            }
            default: {
                LOGGER.error("wrong command");
                final ErrorMessage msg = new ErrorMessage("Invalid command", objectMapper);
                try {
                    session.sendMessage(new TextMessage(msg.getJson()));
                } catch (IOException ignored) {
                }
            }

        }
    }

    private void endSession(String userId, WebSocketSession session) {
        final LobbyService.ErrorCodes codes = lobbyService.removeUser(userId);
        //noinspection EnumSwitchStatementWhichMissesCases,SwitchStatementWithoutDefaultBranch
        switch (codes) {
            case OK: {
                final OkMessage msg = new OkMessage("Ok", objectMapper);
                try {
                    session.sendMessage(new TextMessage(msg.getJson()));
                } catch (IOException ignored) {
                }
                break;
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
