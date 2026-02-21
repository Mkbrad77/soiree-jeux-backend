package com.soiree.repository;

import com.soiree.model.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour gérer les résultats
 */
@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {

    // Trouver tous les résultats d'un jeu spécifique
    List<GameResult> findByGameId(Long gameId);

    // Trouver tous les résultats d'une équipe
    List<GameResult> findByTeamId(Long teamId);
}