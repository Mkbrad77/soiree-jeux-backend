package com.soiree.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Représente le résultat d'une équipe pour un jeu spécifique
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonIgnore
    private Game game;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private int position;

    private int pointsEarned;

    private boolean drankShot;

    // Constructeur pratique (points selon position : 1er=4, 2e=3, 3e=2, 4e=1)
    public GameResult(Game game, Team team, int position) {
        this.game = game;
        this.team = team;
        this.position = position;
        this.pointsEarned = calculatePoints(position);
        this.drankShot = (position > 1);
    }

    /** Constructeur avec points explicites (ex. Whisky Undercover) */
    public GameResult(Game game, Team team, int position, int pointsEarned) {
        this.game = game;
        this.team = team;
        this.position = position;
        this.pointsEarned = pointsEarned;
        this.drankShot = (position > 1);
    }

    /**
     * Shots à boire : 1er = 0, 2ème = 1, 3ème = 2, 4ème = 3, etc.
     */
    public int getShotsCount() {
        return Math.max(0, position - 1);
    }

    /**
     * Points selon la position : 1er = 4, 2e = 3, 3e = 2, 4e = 1, 5e+ = 0
     */
    private int calculatePoints(int position) {
        return switch (position) {
            case 1 -> 4;
            case 2 -> 3;
            case 3 -> 2;
            case 4 -> 1;
            default -> 0;
        };
    }
}
