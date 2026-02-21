package com.soiree.repository;

import com.soiree.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour gérer les jeux
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    // Méthode personnalisée : Spring génère automatiquement la requête SQL
    // à partir du nom de la méthode
    List<Game> findAllByOrderByOrderAsc();
}
