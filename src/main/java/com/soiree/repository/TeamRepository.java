
package com.soiree.repository;

import com.soiree.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour gérer les équipes
 * JpaRepository fournit automatiquement les méthodes CRUD de base :
 * - save() : créer/modifier
 * - findAll() : récupérer toutes les équipes
 * - findById() : trouver par ID
 * - delete() : supprimer
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

}