package com.example.FinalRing.Service;

import com.example.FinalRing.Exception.PlayerNotFoundException;
import com.example.FinalRing.Request.MatchRequest;
import com.example.FinalRing.Response.MatchResponse;
import com.example.FinalRing.entity.Match;
import com.example.FinalRing.entity.MatchStatus;
import com.example.FinalRing.entity.Player;
import com.example.FinalRing.repo.MatchPlayerRepo;
import com.example.FinalRing.repo.MatchRepo;
import com.example.FinalRing.repo.PlayerRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchService {
    private final PlayerRepo playerRepo;
    private final MatchRepo matchRepo;
    private final MatchPlayerService matchPlayerService;
    private final MatchPlayerRepo matchPlayerRepo;

    public MatchService(PlayerRepo playerRepo, MatchRepo matchRepo, MatchPlayerService matchPlayerService, MatchPlayerRepo matchPlayerRepo) {
        this.playerRepo = playerRepo;
        this.matchRepo = matchRepo;
        this.matchPlayerService = matchPlayerService;
        this.matchPlayerRepo = matchPlayerRepo;
    }
@Transactional
    public MatchResponse createMatch(MatchRequest matchRequest, String playerEmail){
        Player player=playerRepo.findByEmail(playerEmail).orElseThrow(()-> new PlayerNotFoundException("Player doesn't exist"));
        Match match=new Match();
        match.setMaxPlayers(matchRequest.getMaxPlayers());
        match.setMatchStatus(MatchStatus.WAITING);
        matchRepo.save(match);
        matchPlayerService.createMatchPlayer(match,player);
        MatchResponse response=new MatchResponse(match);
        response.setCurrentPlayers((int)matchPlayerRepo.countByMatchId(match.getId()));
        return response;
    }

    public MatchResponse getMatch(long id){
        Match match=matchRepo.findById(id).orElseThrow();
        MatchResponse response=new MatchResponse(match);
        return response;
    }
}
