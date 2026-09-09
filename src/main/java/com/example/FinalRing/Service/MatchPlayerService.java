package com.example.FinalRing.Service;

import com.example.FinalRing.entity.Match;
import com.example.FinalRing.entity.MatchPlayer;
import com.example.FinalRing.entity.MatchPlayerStatus;
import com.example.FinalRing.entity.Player;
import com.example.FinalRing.repo.MatchPlayerRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MatchPlayerService {

    private final MatchPlayerRepo matchPlayerRepo;

    public MatchPlayerService(MatchPlayerRepo matchPlayerRepo) {
        this.matchPlayerRepo = matchPlayerRepo;
    }

    public MatchPlayer createMatchPlayer(Match match, Player player){
        MatchPlayer matchPlayer=new MatchPlayer();
        matchPlayer.setPlayer(player);
        matchPlayer.setMatch(match);
        matchPlayer.setMatchPlayerStatus(MatchPlayerStatus.ALIVE);
        matchPlayer.setKills(0);
        matchPlayer.setDeaths(0);
        matchPlayer.setJoinedAt(Instant.now());
        matchPlayerRepo.save(matchPlayer);
        return matchPlayer;
    }
}
