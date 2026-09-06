package com.example.FinalRing.repo;

import com.example.FinalRing.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepo extends JpaRepository<Player,Long> {
    boolean existsByEmail(String email);
    Optional<Player> findByEmail(String email);
}
