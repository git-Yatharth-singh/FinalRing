package com.example.FinalRing.Service;

import com.example.FinalRing.Exception.*;
import com.example.FinalRing.Request.MatchRequest;
import com.example.FinalRing.Response.MatchResponse;
import com.example.FinalRing.Response.MatchResultResponse;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        match.setCreator(player);
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

    public MatchResponse startMatch(long matchId,String creatorEmail) throws NotCreatorException {
        Match match=matchRepo.findById(matchId).orElseThrow(()->new MatchNotFoundException("Match not found"));
        Player player=playerRepo.findByEmail(creatorEmail).orElseThrow(()->new NotCreatorException("Not the creator of the match"));
        if(player!=match.getCreator()){
            throw new NotCreatorException("Only the leader can start the match");
        }
        if(match.getMatchStatus()!=MatchStatus.WAITING){
            throw new MatchStartedException("Match has ended");
        }
        long players=matchPlayerRepo.countByMatchId(matchId);
        if(players<2){
            throw new InsufficientException("A match must contain atleast 2 players");
        }
        match.setMatchStatus(MatchStatus.RUNNING);
        match.setStartedAt(Instant.now());
        matchRepo.save(match);
        MatchResponse response=new MatchResponse(match);
        response.setCurrentPlayers((int)players);
        return response;
    }

    public MatchResponse finishMatch(long matchId){
        Match match=matchRepo.findById(matchId).orElseThrow(()->new MatchNotFoundException("Match not found"));
        if(match.getMatchStatus()!=MatchStatus.RUNNING){
            throw new MatchNotRunningException("Match is not running");
        }
        match.setMatchStatus(MatchStatus.FINISHED);
        match.setFinishedAt(Instant.now());
        matchRepo.save(match);
        MatchResponse matchResponse=new MatchResponse(match);
        return matchResponse;
    }

    public List<MatchResultResponse> matchResult(long matchId){
        List<MatchResultResponse> response=new ArrayList<>();
        Match match=matchRepo.findById(matchId).orElseThrow(()->new MatchNotFoundException("Match not found"));
        if(match.getMatchStatus()==MatchStatus.RUNNING || match.getMatchStatus()==MatchStatus.WAITING){
            throw new MatchNotRunningException("Match has not ended");
        }
        List<MatchPlayer> players=matchPlayerRepo.findByMatchId(matchId);
        players.sort(Comparator.comparing(MatchPlayer::getKills).reversed().thenComparing(MatchPlayer::getDeaths));
        for(int i=0;i<players.size();i++){
            MatchResultResponse resultResponse=new MatchResultResponse();
            resultResponse.setPlayerName(players.get(i).getPlayer().getName());
            resultResponse.setKills(players.get(i).getKills());
            resultResponse.setDeaths(players.get(i).getDeaths());
            resultResponse.setRank(i+1);
            response.add(resultResponse);
        }
        return response;
    }
}

