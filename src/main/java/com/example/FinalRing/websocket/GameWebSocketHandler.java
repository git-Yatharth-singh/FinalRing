package com.example.FinalRing.websocket;

import com.example.FinalRing.Exception.PlayerNotFoundException;
import com.example.FinalRing.Exception.PlayerNotInMatchException;
import com.example.FinalRing.entity.Player;
import com.example.FinalRing.repo.MatchPlayerRepo;
import com.example.FinalRing.repo.PlayerRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final PlayerRepo playerRepo;
    private final MatchPlayerRepo matchPlayerRepo;

    private final Map<WebSocketSession, Player> sessions =
            new ConcurrentHashMap<>();

    public GameWebSocketHandler(
            PlayerRepo playerRepo,
            MatchPlayerRepo matchPlayerRepo) {

        this.playerRepo = playerRepo;
        this.matchPlayerRepo = matchPlayerRepo;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {

        String email = (String) session.getAttributes().get("email");

        Player player = playerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new PlayerNotFoundException("Player not found"));

        sessions.put(session, player);

        System.out.println(
                "Client connected: " +
                        session.getId() +
                        " Player: " +
                        player.getName()
        );
    }

    @Override
    protected void handleTextMessage(
            WebSocketSession session,
            TextMessage message) throws IOException {

        long matchId = Long.parseLong(
                UriComponentsBuilder
                        .fromUri(session.getUri())
                        .build()
                        .getQueryParams()
                        .getFirst("matchId")
        );
        Player senderPlayer = sessions.get(session);
        if (senderPlayer == null) {
            throw new IllegalStateException("WebSocket session is not authenticated");
        }
        long senderId = senderPlayer.getId();

        // Check whether sender actually belongs to this match
        if (!matchPlayerRepo.existsByPlayerIdAndMatchId(
                senderId, matchId)) {

            throw new PlayerNotInMatchException(
                    "Player do not belong to this match"
            );
        }

        String receivedMessage = message.getPayload();

        // Send message only to players in the same match
        for (Map.Entry<WebSocketSession, Player> entry
                : sessions.entrySet()) {

            WebSocketSession recipientSession = entry.getKey();
            Player recipientPlayer = entry.getValue();

            // Don't send message back to sender
            if (recipientSession.getId().equals(session.getId())) {
                continue;
            }

            // Check whether recipient belongs to the same match
            if (matchPlayerRepo.existsByPlayerIdAndMatchId(
                    recipientPlayer.getId(), matchId)) {

                recipientSession.sendMessage(
                        new TextMessage(receivedMessage)
                );
            }
        }
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            org.springframework.web.socket.CloseStatus status)
            throws Exception {

        sessions.remove(session);

        System.out.println(
                "Client disconnected: " +
                        session.getId()
        );
    }
}