package com.soiree.controller;

import com.soiree.model.Player;
import com.soiree.model.Team;
import com.soiree.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour gérer les équipes
 * @CrossOrigin : permet les requêtes depuis Angular
 */
@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    /**
     * GET /api/teams
     * Récupérer toutes les équipes triées par points
     */
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeamsSortedByPoints());
    }

    /**
     * POST /api/teams/create
     * Créer des équipes équilibrées
     * Body : { "players": [...], "numberOfTeams": 4 }
     */
    @PostMapping("/create")
    public ResponseEntity<List<Team>> createTeams(@RequestBody TeamCreationRequest request) {
        List<Team> teams = teamService.createBalancedTeams(
                request.getPlayers(),
                request.getNumberOfTeams()
        );
        return ResponseEntity.ok(teams);
    }

    /**
     * POST /api/teams/{teamId}/players
     * Ajouter un joueur à une équipe
     */
    @PostMapping("/{teamId}/players")
    public ResponseEntity<Team> addPlayer(
            @PathVariable Long teamId,
            @RequestBody Player player) {
        Team team = teamService.addPlayerToTeam(teamId, player);
        return ResponseEntity.ok(team);
    }

    /**
     * DELETE /api/teams/{teamId}/players/{playerId}
     * Retirer un joueur d'une équipe
     */
    @DeleteMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<Void> removePlayer(
            @PathVariable Long teamId,
            @PathVariable Long playerId) {
        teamService.removePlayerFromTeam(teamId, playerId);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/teams/reset
     * Réinitialiser tous les scores
     */
    @PostMapping("/reset")
    public ResponseEntity<Void> resetScores() {
        teamService.resetAllScores();
        return ResponseEntity.ok().build();
    }
}

/**
 * Classe pour recevoir la requête de création d'équipes
 */
@lombok.Data
class TeamCreationRequest {
    private List<Player> players;
    private int numberOfTeams;
}