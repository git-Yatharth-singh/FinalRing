package com.example.FinalRing.Controller;

import com.example.FinalRing.Request.LoginRequest;
import com.example.FinalRing.Request.SignupRequest;
import com.example.FinalRing.Service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PlayerService playerService;

    public AuthController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> savePlayer(@Valid @RequestBody SignupRequest request){
        playerService.savePlayer(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginPlayer(@Valid @RequestBody LoginRequest request){
        String response= playerService.loginPlayer(request);
        return ResponseEntity.ok(response);
    }


}
