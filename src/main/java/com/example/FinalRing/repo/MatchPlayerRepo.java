package com.example.FinalRing.repo;

import com.example.FinalRing.entity.MatchPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchPlayerRepo extends JpaRepository<MatchPlayer,Long> {
    long countByMatchId(long matchId);
    boolean existsByPlayerIdAndMatchId(long playerId, long matchId);
    Optional<MatchPlayer> findByPlayerIdAndMatchId(long playerId, long matchId);
    List<MatchPlayer> findByMatchId(long matchId);
}
