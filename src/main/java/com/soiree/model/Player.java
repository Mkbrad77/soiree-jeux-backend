package com.soiree.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Représente un joueur participant à la soirée jeux
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonBackReference
    private Team team;

    private int shotsCount = 0; // Nombre de shots bus par ce joueur

    public Player(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
        this.shotsCount = 0;
    }

    // Méthode pour ajouter des shots
    public void addShots(int count) {
        this.shotsCount += count;
    }

    public enum Gender {
        HOMME,
        FEMME
    }
}
