package com.soiree.repository;

import com.soiree.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour gérer les joueurs
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}