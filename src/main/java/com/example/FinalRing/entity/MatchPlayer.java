package com.example.FinalRing.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "match_player")
public class MatchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Match match;

    @Enumerated(EnumType.STRING)
    private MatchPlayerStatus matchPlayerStatus;

    private int kills;

    private Instant joinedAt;

    private Integer rank;

    private int deaths;
}
