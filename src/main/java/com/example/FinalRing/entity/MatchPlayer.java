package com.example.FinalRing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "match_player",
        indexes = {
                @Index(name = "idx_match_player_match_id", columnList = "match_id")
        })
public class MatchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Match match;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchPlayerStatus matchPlayerStatus;

    private int kills;
    @Column(nullable = false)
    private Instant joinedAt;

    private Integer rank;

    private int deaths;
}
