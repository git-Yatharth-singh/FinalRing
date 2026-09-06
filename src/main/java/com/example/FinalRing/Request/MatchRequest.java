package com.example.FinalRing.Request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
public class MatchRequest {
    @Min(value = 50, message = "A match must have at least 50 players")
    private int maxPlayers;

}
