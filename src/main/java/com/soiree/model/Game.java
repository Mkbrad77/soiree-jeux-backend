package com.soiree.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un jeu durant la soirée
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "`order`")
    private int order; // Ordre du jeu dans la soirée

    @Enumerated(EnumType.STRING)
    private GameType type; // Type de jeu (duo, représentant, etc.)

    private boolean completed; // Jeu terminé ou non

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<GameResult> results = new ArrayList<>();

    /**
     * Constructeur utilisé dans GameService.initializeGames()
     */
    public Game(String name, String description, int order, GameType type) {
        this.name = name;
        this.description = description;
        this.order = order;
        this.type = type;
        this.completed = false;
        this.results = new ArrayList<>();
    }

    /**
     * Ajouter un résultat à ce jeu
     */
    public void addResult(GameResult result) {
        results.add(result);
        result.setGame(this);
    }

    /**
     * Types possibles de jeux
     */
    public enum GameType {
        TOUS_ENSEMBLE,   // Toute l'équipe joue
        REPRESENTANT,    // Un seul représentant
        DUO,             // 2 joueurs
        UN_VS_UN         // 1 contre 1
    }
}
