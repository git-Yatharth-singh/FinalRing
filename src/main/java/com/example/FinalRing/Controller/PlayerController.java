package com.example.FinalRing.Controller;

import com.example.FinalRing.Response.ProfileResponse;
import com.example.FinalRing.Service.JwtService;
import com.example.FinalRing.Service.ProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player")
public class PlayerController {
    private final ProfileService profileService;

    public PlayerController(ProfileService profileService) {
        this.profileService = profileService;
    }
    @GetMapping("/profile")
    public ProfileResponse playerProfile(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String email= authentication.getPrincipal().toString();
        return profileService.playerProfile(email);
    }


}
