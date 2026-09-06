package com.example.FinalRing.Service;

import com.example.FinalRing.Exception.EmailAlreadyExistsException;
import com.example.FinalRing.Exception.InvalidCredentialsException;
import com.example.FinalRing.Exception.PlayerNotFoundException;
import com.example.FinalRing.Request.LoginRequest;
import com.example.FinalRing.Request.SignupRequest;
import com.example.FinalRing.entity.Player;
import com.example.FinalRing.repo.PlayerRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final PlayerRepo playerRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public PlayerService(PlayerRepo playerRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.playerRepo = playerRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void savePlayer(SignupRequest request){
        Player player=new Player();
        player.setEmail(request.getEmail());
        if(playerRepo.existsByEmail(player.getEmail()))throw new EmailAlreadyExistsException("Email already exists");
        player.setName(request.getName());
        player.setPassword(passwordEncoder.encode(request.getPassword()));
        playerRepo.save(player);
    }

    public String loginPlayer(LoginRequest request){

        Player player=playerRepo.findByEmail(request.getEmail()).orElseThrow(()->new PlayerNotFoundException("Invalid email or password"));
        if(!passwordEncoder.matches(request.getPassword(), player.getPassword())){
            throw new PlayerNotFoundException("Invalid email or password");
        }
        return jwtService.generateToken(player.getEmail());
    }
}
