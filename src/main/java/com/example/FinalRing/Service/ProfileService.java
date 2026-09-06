package com.example.FinalRing.Service;

import com.example.FinalRing.Exception.PlayerNotFoundException;
import com.example.FinalRing.Response.ProfileResponse;
import com.example.FinalRing.entity.Player;
import com.example.FinalRing.repo.PlayerRepo;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final PlayerRepo playerRepo;

    public ProfileService(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    public ProfileResponse playerProfile(String email){
        ProfileResponse response = new ProfileResponse();
        Player player=playerRepo.findByEmail(email).orElseThrow(()->new PlayerNotFoundException("Player not found"));
        response.setEmail(player.getEmail());
        response.setName(player.getName());
        return response;
    }
}
