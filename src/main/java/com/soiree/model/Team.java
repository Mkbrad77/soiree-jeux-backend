package com.soiree.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une équipe dans la soirée jeux
 * @Data : Lombok génère automatiquement getters, setters, toString, equals, hashCode
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color; // Couleur pour l'interface (ex: "#FF5733")

    private int totalPoints;

    private int shotsCount; // Nombre de shots bus

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Player> players = new ArrayList<>();

    // Constructeur pratique pour créer une équipe rapidement
    public Team(String name, String color) {
        this.name = name;
        this.color = color;
        this.totalPoints = 0;
        this.shotsCount = 0;
        this.players = new ArrayList<>();
    }

    // Méthode pour ajouter des points
    public void addPoints(int points) {
        this.totalPoints += points;
    }

    // Méthode pour ajouter un shot
    public void addShot() {
        this.shotsCount++;
    }

    // Méthode pour ajouter plusieurs shots
    public void addShots(int count) {
        this.shotsCount += count;
    }

    // Méthode pour ajouter un joueur à l'équipe
    public void addPlayer(Player player) {
        players.add(player);
        player.setTeam(this);
    }

    // Méthode pour retirer un joueur
    public void removePlayer(Player player) {
        players.remove(player);
        player.setTeam(null);
    }
}