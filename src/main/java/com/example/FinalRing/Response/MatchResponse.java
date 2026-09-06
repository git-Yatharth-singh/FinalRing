package com.example.FinalRing.Response;

import com.example.FinalRing.entity.Match;
import com.example.FinalRing.entity.MatchStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class MatchResponse {
    private long id;
    private MatchStatus matchStatus;
    private int maxPlayers;
    private int currentPlayers;
    private Instant createdAt;
    private Instant startedAt;

    public MatchResponse(Match match) {
        this.id = match.getId();
        this.matchStatus = match.getMatchStatus();
        this.maxPlayers = match.getMaxPlayers();
        this.createdAt = match.getCreatedAt();
        this.startedAt = match.getStartedAt();
    }
}
