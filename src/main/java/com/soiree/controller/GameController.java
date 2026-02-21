package com.soiree.controller;

import com.soiree.model.Game;
import com.soiree.model.GameResult;
import com.soiree.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller REST pour gérer les jeux
 */
@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    /**
     * POST /api/games/initialize
     * Initialiser tous les jeux de la soirée
     */
    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeGames() {
        gameService.initializeGames();
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/games
     * Récupérer tous les jeux
     */
    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    /**
     * POST /api/games/{gameId}/results
     * Enregistrer les résultats d'un jeu
     * Body : { "positions": { "1": 1, "2": 2, "3": 3, "4": 4 } }
     * où les clés sont les teamId et les valeurs les positions
     */
    @PostMapping("/{gameId}/results")
    public ResponseEntity<?> saveResults(
            @PathVariable Long gameId,
            @RequestBody GameResultRequest request) {
        try {
            gameService.saveGameResults(gameId, request.getPositions());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage(), "error", "VALIDATION_ERROR"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage(), "error", "GAME_ERROR"));
        }
    }

    /**
     * GET /api/games/{gameId}/results
     * Récupérer les résultats d'un jeu
     */
    @GetMapping("/{gameId}/results")
    public ResponseEntity<List<GameResult>> getGameResults(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.getGameResults(gameId));
    }

    /**
     * GET /api/games/teams/{teamId}/history
     * Récupérer l'historique d'une équipe
     */
    @GetMapping("/teams/{teamId}/history")
    public ResponseEntity<List<GameResult>> getTeamHistory(@PathVariable Long teamId) {
        return ResponseEntity.ok(gameService.getTeamHistory(teamId));
    }

    /**
     * POST /api/games/{gameId}/gage-bonus
     * Enregistrer les points bonus d'un gage
     * Body : { "teamId": 1, "points": 2 }
     */
    @PostMapping("/{gameId}/gage-bonus")
    public ResponseEntity<?> saveGageBonus(
            @PathVariable Long gameId,
            @RequestBody GageBonusRequest request) {
        try {
            gameService.saveGageBonus(gameId, request.getTeamId(), request.getPoints());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage(), "error", "GAGE_ERROR"));
        }
    }

    /**
     * POST /api/games/{gameId}/undercover-outcome
     * Enregistrer le résultat Whisky Undercover (Mister White)
     * Body : { "misterWhiteTeamId": 1, "eliminated": true, "eliminatedInRound": 2 }
     */
    @PostMapping("/{gameId}/undercover-outcome")
    public ResponseEntity<?> saveUndercoverOutcome(
            @PathVariable Long gameId,
            @RequestBody UndercoverOutcomeRequest request) {
        try {
            gameService.saveUndercoverOutcome(gameId, request.getMisterWhiteTeamId(),
                    request.getEliminated(), request.getEliminatedInRound());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage(), "error", "VALIDATION_ERROR"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage(), "error", "GAME_ERROR"));
        }
    }
}

/**
 * Classe pour recevoir les résultats
 */
@lombok.Data
class GameResultRequest {
    private Map<Long, Integer> positions; // teamId -> position
}

/**
 * Classe pour recevoir les points bonus d'un gage
 */
@lombok.Data
class GageBonusRequest {
    private Long teamId;
    private Integer points;
}

/**
 * Issue Whisky Undercover : équipe Mister White, éliminé ou non, tour d'élimination (1-3)
 */
@lombok.Data
class UndercoverOutcomeRequest {
    private Long misterWhiteTeamId;
    private Boolean eliminated;
    private Integer eliminatedInRound; // 1, 2 ou 3 si eliminated
}