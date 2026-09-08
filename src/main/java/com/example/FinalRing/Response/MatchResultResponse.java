package com.example.FinalRing.Response;

import com.example.FinalRing.entity.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchResultResponse {
    private String playerName;
    private int deaths;
    private int kills;
    private int rank;
}
