package com.example.FinalRing.repo;

import com.example.FinalRing.entity.Match;
import com.example.FinalRing.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepo extends JpaRepository<Match,Long> {

}
