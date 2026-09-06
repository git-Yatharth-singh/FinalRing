package com.example.FinalRing.repo;

import com.example.FinalRing.entity.MatchPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchPlayerRepo extends JpaRepository<MatchPlayer,Long> {
    long countByMatchId(long matchId);
}
