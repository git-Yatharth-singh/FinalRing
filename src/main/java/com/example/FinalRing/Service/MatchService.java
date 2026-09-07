package com.example.FinalRing.Service;

import com.example.FinalRing.Exception.*;
import com.example.FinalRing.Request.MatchRequest;
import com.example.FinalRing.Response.MatchResponse;
import com.example.FinalRing.entity.Match;
import com.example.FinalRing.entity.MatchPlayer;
import com.example.FinalRing.entity.MatchStatus;
import com.example.FinalRing.entity.Player;
import com.example.FinalRing.repo.MatchPlayerRepo;
import com.example.FinalRing.repo.MatchRepo;
import com.example.FinalRing.repo.PlayerRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public MatchResponse leaveMatch(long matchId) {
        Match match=matchRepo.findById(matchId).orElseThrow(()->new MatchNotFoundException("Match do not exist"));
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getPrincipal().toString();
        Player player=playerRepo.findByEmail(email).orElseThrow(()->new PlayerNotFoundException("Player not found"));
        MatchPlayer matchPlayer = matchPlayerRepo
                .findByPlayerIdAndMatchId(player.getId(), matchId)
                .orElseThrow(() -> new PlayerNotInMatchException("Player is not in this match"));

        matchPlayerRepo.delete(matchPlayer);
        MatchResponse matchResponse=new MatchResponse(match);
        matchResponse.setCurrentPlayers((int)matchPlayerRepo.countByMatchId(match.getId()));
        return matchResponse;
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
        Match match=matchRepo.findById(id).orElseThrow(()-> new MatchNotFoundException("Match not found"));
        MatchResponse response=new MatchResponse(match);
        response.setCurrentPlayers((int) matchPlayerRepo.countByMatchId(match.getId()));
        return response;
    }
@Transactional
    public MatchResponse joinMatch(long matchId) {
        Match match=matchRepo.findById(matchId).orElseThrow(()->new MatchNotFoundException("Match do not exist"));
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getPrincipal().toString();
        Player player=playerRepo.findByEmail(email).orElseThrow(()->new PlayerNotFoundException("Player not found"));
        if(match.getMatchStatus()==MatchStatus.WAITING){
            if(matchPlayerRepo.countByMatchId(matchId)<match.getMaxPlayers()){
                if(!matchPlayerRepo.existsByPlayerIdAndMatchId(player.getId(),matchId)){
                    matchPlayerService.createMatchPlayer(match,player);
                }
                else{
                    throw new PlayerAlreadyJoined("Player already in the match");
                }
            }else{
                throw new MatchFullException("Maximum player reached");
            }
        }
        else{
            throw new MatchStartedException("Match already started");
        }
        MatchResponse matchResponse=new MatchResponse(match);
        matchResponse.setCurrentPlayers((int)matchPlayerRepo.countByMatchId(matchId));
        return matchResponse;
    }
}

