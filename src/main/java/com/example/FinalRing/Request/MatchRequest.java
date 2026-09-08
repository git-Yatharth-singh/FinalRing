package com.example.FinalRing.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
public class MatchRequest {
    @Min(value = 2, message = "A match must have at least 2 players")
    @Max(value=6, message = "A match cannot have more than 6 players")
    private int maxPlayers;

}
