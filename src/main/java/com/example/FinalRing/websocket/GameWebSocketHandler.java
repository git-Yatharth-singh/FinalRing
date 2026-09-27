package com.example.FinalRing.websocket;

import com.example.FinalRing.Exception.MatchNotFoundException;
import com.example.FinalRing.Exception.PlayerAlreadyJoined;
import com.example.FinalRing.Exception.PlayerNotFoundException;
import com.example.FinalRing.Exception.PlayerNotInMatchException;
import com.example.FinalRing.entity.Player;
import com.example.FinalRing.repo.MatchPlayerRepo;
import com.example.FinalRing.repo.MatchRepo;
import com.example.FinalRing.repo.PlayerRepo;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final PlayerRepo playerRepo;
    private final MatchPlayerRepo matchPlayerRepo;
    private final MatchRepo matchRepo;
    private final Map<WebSocketSession, Player> sessions =
            new ConcurrentHashMap<>();

    private final Map<Long, Set<WebSocketSession>> matchSessions =
            new ConcurrentHashMap<>();

    private final Map<WebSocketSession,Long>sessionMatches=new ConcurrentHashMap<>();

    private final Map<Long,WebSocketSession>playerSessions=new ConcurrentHashMap<>();

    public GameWebSocketHandler(
            PlayerRepo playerRepo,
            MatchPlayerRepo matchPlayerRepo, MatchRepo matchRepo) {

        this.playerRepo = playerRepo;
        this.matchPlayerRepo = matchPlayerRepo;
        this.matchRepo = matchRepo;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {

        String email = (String) session.getAttributes().get("email");

        Player player = playerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new PlayerNotFoundException("Player not found"));
        long matchId = Long.parseLong(
                UriComponentsBuilder
                        .fromUri(session.getUri())
                        .build()
                        .getQueryParams()
                        .getFirst("matchId")
        );
        if(!matchRepo.existsById(matchId)){
            throw new MatchNotFoundException("Match does not exist");
        }

        if(!matchPlayerRepo.existsByPlayerIdAndMatchId(player.getId(),matchId)){
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }
        WebSocketSession existingSession=playerSessions.putIfAbsent(player.getId(),session);
        if(existingSession!=null){
            System.out.println(
                    "DUPLICATE CONNECTION: Player " +
                            player.getId() +
                            " already has session " +
                            existingSession.getId()
            );
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }
        sessions.put(session, player);
        sessionMatches.put(session,matchId);
        matchSessions
                .computeIfAbsent(
                        matchId,
                        id -> ConcurrentHashMap.newKeySet()
                )
                .add(session);
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
        Long sessionMatchid=sessionMatches.get(session);
        if(sessionMatchid==null || matchId!=sessionMatchid){
            session.sendMessage(
                    new TextMessage("You are not connected to this match")
            );
            return;
        }
        Player senderPlayer = sessions.get(session);
        if (senderPlayer == null) {
            throw new IllegalStateException("WebSocket session is not authenticated");
        }
        long senderId = senderPlayer.getId();

        // Check whether sender actually belongs to this match
        if (!matchPlayerRepo.existsByPlayerIdAndMatchId(
                senderId, matchId)) {

            session.sendMessage(
                    new TextMessage("You are not a member of this match")
            );
            return;
        }

        String receivedMessage = message.getPayload();

        Set<WebSocketSession> recipients = matchSessions.get(matchId);

        if (recipients == null) {
            return;
        }

        for (WebSocketSession recipientSession : recipients) {

            if (recipientSession.getId().equals(session.getId())) {
                continue;
            }

            recipientSession.sendMessage(
                    new TextMessage(receivedMessage)
            );
        }

    }
    @Override
    public void handleTransportError(
            WebSocketSession session,
            Throwable exception) throws Exception {

        System.out.println(
                "WebSocket transport error: " +
                        exception.getMessage()
        );

        exception.printStackTrace();
    }
    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            org.springframework.web.socket.CloseStatus status)
            throws Exception {
        System.out.println(
                "WebSocket closed. Code: " +
                        status.getCode() +
                        " Reason: " +
                        status.getReason()
        );

        Player player=sessions.get(session);
        Long matchId=sessionMatches.get(session);
        if(player!=null){
            playerSessions.remove(player.getId(),session);
        }
        if(matchId!=null){
            Set<WebSocketSession> matchSessions1=matchSessions.get(matchId);
            if(matchSessions1!=null){
                matchSessions1.remove(session);
                if (matchSessions1.isEmpty()) {
                    matchSessions.remove(matchId);
                }
            }
        }

        sessions.remove(session);


        System.out.println(
                "Client disconnected: " +
                        session.getId()
        );
    }
}