package com.example.FinalRing.Controller;

import com.example.FinalRing.Exception.NotCreatorException;
import com.example.FinalRing.Request.MatchRequest;
import com.example.FinalRing.Response.MatchResponse;
import com.example.FinalRing.Response.MatchResultResponse;
import com.example.FinalRing.Service.MatchService;
import com.example.FinalRing.entity.Match;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public MatchResponse createMatch(@Valid @RequestBody MatchRequest request){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getPrincipal().toString();
        return matchService.createMatch(request,email);
    }

    @GetMapping("/{matchId}")
    public MatchResponse getMatch(@PathVariable long matchId){
        return matchService.getMatch(matchId);
    }

    @PostMapping("/{matchId}/join")
    public ResponseEntity<MatchResponse> joinMatch(@PathVariable long matchId){
        MatchResponse response = matchService.joinMatch(matchId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{matchId}/leave")
    public ResponseEntity<MatchResponse> leaveMatch(@PathVariable long matchId){
        MatchResponse response=matchService.leaveMatch(matchId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{matchId}/start")
    public ResponseEntity<MatchResponse> startMatch(@PathVariable long matchId) throws NotCreatorException {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getPrincipal().toString();
        MatchResponse response=matchService.startMatch(matchId,email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{matchId}/finish")
    public ResponseEntity<MatchResponse> finishMatch(@PathVariable long matchId){
        MatchResponse response=matchService.finishMatch(matchId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{matchId}/results")
    public ResponseEntity<List<MatchResultResponse>> resultMatch(@PathVariable long matchId){
        return ResponseEntity.ok(matchService.matchResult(matchId));
    }
}
