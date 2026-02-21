package com.soiree.service;

import com.soiree.model.Player;
import com.soiree.model.Team;
import com.soiree.repository.PlayerRepository;
import com.soiree.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service pour gérer la logique métier des équipes
 * @RequiredArgsConstructor : Lombok crée un constructeur avec les champs final
 */
@Service
@RequiredArgsConstructor
public class TeamService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);
    
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    // Couleurs prédéfinies pour les équipes
    private static final String[] TEAM_COLORS = {
            "#FF6B6B", "#4ECDC4", "#45B7D1", "#FFA07A",
            "#98D8C8", "#F7DC6F", "#BB8FCE", "#85C1E2"
    };

    /**
     * Créer des équipes automatiquement à partir d'une liste de joueurs
     * Cette méthode équilibre hommes/femmes dans chaque équipe
     */
    @Transactional
    public List<Team> createBalancedTeams(List<Player> players, int numberOfTeams) {
        logger.info("═══════════════════════════════════════════════════════════");
        logger.info("👥 CRÉATION DES ÉQUIPES - {} joueurs pour {} équipes", players.size(), numberOfTeams);
        logger.info("═══════════════════════════════════════════════════════════");
        
        // Supprimer les anciennes équipes et joueurs
        teamRepository.deleteAll();
        playerRepository.deleteAll();
        logger.info("🗑️  Anciennes équipes et joueurs supprimés");

        // Séparer hommes et femmes
        List<Player> men = players.stream()
                .filter(p -> p.getGender() == Player.Gender.HOMME)
                .collect(Collectors.toList());

        List<Player> women = players.stream()
                .filter(p -> p.getGender() == Player.Gender.FEMME)
                .collect(Collectors.toList());
        
        logger.info("📊 Répartition: {} hommes, {} femmes", men.size(), women.size());

        // Mélanger aléatoirement
        Collections.shuffle(men);
        Collections.shuffle(women);

        // Créer les équipes
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < numberOfTeams; i++) {
            Team team = new Team(
                    "Équipe " + (i + 1),
                    TEAM_COLORS[i % TEAM_COLORS.length]
            );
            teams.add(team);
        }

        // Répartir les hommes
        for (int i = 0; i < men.size(); i++) {
            Team team = teams.get(i % numberOfTeams);
            Player player = men.get(i);
            playerRepository.save(player);
            team.addPlayer(player);
        }

        // Répartir les femmes
        for (int i = 0; i < women.size(); i++) {
            Team team = teams.get(i % numberOfTeams);
            Player player = women.get(i);
            playerRepository.save(player);
            team.addPlayer(player);
        }

        // Sauvegarder toutes les équipes
        List<Team> savedTeams = teamRepository.saveAll(teams);
        
        logger.info("✅ {} équipes créées avec succès:", savedTeams.size());
        for (Team team : savedTeams) {
            long hommes = team.getPlayers().stream().filter(p -> p.getGender() == Player.Gender.HOMME).count();
            long femmes = team.getPlayers().stream().filter(p -> p.getGender() == Player.Gender.FEMME).count();
            logger.info("   🎨 {} ({}): {} joueurs ({}H/{}F) - Points: {} - Shots: {}", 
                team.getName(), team.getColor(), 
                team.getPlayers().size(), hommes, femmes,
                team.getTotalPoints(), team.getShotsCount());
            for (Player player : team.getPlayers()) {
                logger.info("      • {} ({})", player.getName(), player.getGender());
            }
        }
        logger.info("═══════════════════════════════════════════════════════════");
        
        return savedTeams;
    }

    /**
     * Récupérer toutes les équipes triées par points
     */
    public List<Team> getAllTeamsSortedByPoints() {
        List<Team> teams = teamRepository.findAll();
        teams.sort((t1, t2) -> Integer.compare(t2.getTotalPoints(), t1.getTotalPoints()));
        return teams;
    }

    /**
     * Ajouter un joueur à une équipe existante
     */
    @Transactional
    public Team addPlayerToTeam(Long teamId, Player player) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));

        logger.info("➕ AJOUT DE JOUEUR: {} ({}) → {}", player.getName(), player.getGender(), team.getName());
        
        playerRepository.save(player);
        team.addPlayer(player);
        Team savedTeam = teamRepository.save(team);
        
        logger.info("   ✅ {} a maintenant {} joueurs", savedTeam.getName(), savedTeam.getPlayers().size());
        
        return savedTeam;
    }

    /**
     * Retirer un joueur d'une équipe
     */
    @Transactional
    public void removePlayerFromTeam(Long teamId, Long playerId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé"));

        logger.info("➖ RETRAIT DE JOUEUR: {} → retiré de {}", player.getName(), team.getName());

        team.removePlayer(player);
        teamRepository.save(team);
        playerRepository.delete(player);
        
        logger.info("   ✅ {} a maintenant {} joueurs", team.getName(), team.getPlayers().size());
    }

    /**
     * Réinitialiser tous les scores
     */
    @Transactional
    public void resetAllScores() {
        logger.info("🔄 RÉINITIALISATION DES SCORES");
        List<Team> teams = teamRepository.findAll();
        teams.forEach(team -> {
            logger.info("   🔄 {}: {} points → 0, {} shots → 0", 
                team.getName(), team.getTotalPoints(), team.getShotsCount());
            team.setTotalPoints(0);
            team.setShotsCount(0);
        });
        teamRepository.saveAll(teams);
        logger.info("✅ Tous les scores ont été réinitialisés");
    }
}