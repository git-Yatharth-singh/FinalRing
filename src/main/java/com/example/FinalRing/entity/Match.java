package com.example.FinalRing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "match")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;

    private int maxPlayers;

    @CreationTimestamp
    private Instant createdAt;

    @ManyToOne
    private Player creator;

    private Instant startedAt;

    private Instant finishedAt;
}
