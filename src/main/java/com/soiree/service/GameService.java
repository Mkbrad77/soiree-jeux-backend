package com.soiree.service;

import com.soiree.model.Game;
import com.soiree.model.GameResult;
import com.soiree.model.Player;
import com.soiree.model.Team;
import com.soiree.repository.GameRepository;
import com.soiree.repository.GameResultRepository;
import com.soiree.repository.PlayerRepository;
import com.soiree.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Service pour gérer la logique des jeux et résultats
 */
@Service
@RequiredArgsConstructor
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final GameResultRepository gameResultRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    /**
     * Initialiser tous les jeux de la soirée
     */
    @Transactional
    public void initializeGames() {
        logger.info("═══════════════════════════════════════════════════════════");
        logger.info("🎮 INITIALISATION DES JEUX - Début de la soirée");
        logger.info("═══════════════════════════════════════════════════════════");
        
        gameRepository.deleteAll();
        logger.info("🗑️  Anciens jeux supprimés");

        // Soirée anniversaire : 5 jeux uniquement (Blindtest, Mime, Undercover, Speed Dating, Gage)
        List<Game> games = List.of(
                new Game("Blindtest",
                        "🎵 RÈGLES DU BLINDTEST\n\n" +
                        "🎯 CONCEPT :\n" +
                        "• Jeu musical : reconnaissance de morceaux d'afrobeat et musiques nigérianes\n" +
                        "• Environ 15 chansons préparées\n\n" +
                        "📋 DÉROULEMENT :\n" +
                        "• Chaque équipe a un buzzer ou lève la main pour répondre\n" +
                        "• Le modérateur lance une chanson\n" +
                        "• La première équipe à buzzer répond\n" +
                        "• Bonne réponse = +1 point | Mauvaise réponse = -0,5 point (les autres peuvent tenter)\n\n" +
                        "🏆 FIN DE PARTIE :\n" +
                        "• Classement selon le total des points\n" +
                        "• En cas d'égalité : manche de départage avec une chanson bonus\n\n" +
                        "📝 À LA FIN : saisissez le classement des équipes (1re, 2e, 3e, 4e).",
                        1, Game.GameType.TOUS_ENSEMBLE),

                new Game("Jeu de Mime",
                        "🎭 RÈGLES DU JEU DE MIME\n\n" +
                        "👤 PARTICIPANTS :\n" +
                        "• Chaque équipe désigne un représentant (mimeur)\n" +
                        "• Les autres membres de l'équipe doivent deviner\n\n" +
                        "⏱️ DURÉE : 2 minutes par équipe • 3 mots à faire deviner (préparés à l'avance)\n\n" +
                        "🎮 DÉROULEMENT :\n" +
                        "• Le mimeur mime les mots sans parler\n" +
                        "• Son équipe doit deviner • Chaque mot deviné = +1 point\n" +
                        "• Les autres équipes ne peuvent ni aider ni interférer\n\n" +
                        "🏆 CLASSEMENT : selon le nombre de mots devinés. En cas d'égalité : mot bonus avec temps limité.\n\n" +
                        "📝 À LA FIN : saisissez le classement des équipes (1re à la dernière).",
                        2, Game.GameType.REPRESENTANT),

                new Game("Whisky Undercover",
                        "🕵️ RÈGLES DU WHISKY UNDERCOVER (MISTER WHITE)\n\n" +
                        "🎯 CONCEPT :\n" +
                        "• Un gobelet contient de la vodka, les autres de l'eau\n" +
                        "• Mister White = le représentant qui a la vodka ; il ne doit pas se faire repérer\n" +
                        "• Une personne neutre (qui ne joue pas) remplit les gobelets\n\n" +
                        "📋 PRÉPARATION :\n" +
                        "• Nombre de gobelets = nombre d'équipes\n" +
                        "• Un représentant par équipe prend un gobelet (sans savoir qui a quoi)\n\n" +
                        "🔄 TOURS (ex. 3 max) :\n" +
                        "• Tout le monde boit/sippe → Conertation (ex. 2 min) → Vote pour suspecter une personne\n" +
                        "• La personne avec le plus de voix est éliminée et révèle si elle avait la vodka\n\n" +
                        "🏆 RÉSULTAT :\n" +
                        "• Si Mister White est éliminé → son équipe 0 pt, les autres 2 pts chacune\n" +
                        "• Si après 3 tours Mister White n'est pas éliminé → son équipe 3 pts, les autres 0\n\n" +
                        "📝 À LA FIN : indiquez quelle équipe était Mister White et s'il a été éliminé (et à quel tour).",
                        3, Game.GameType.TOUS_ENSEMBLE),

                new Game("Speed Dating",
                        "💕 RÈGLES DU SPEED DATING\n\n" +
                        "🎯 BUT : désigner le garçon qui fait le meilleur speech de drague devant une fille (éloquence, charisme, humour).\n\n" +
                        "📋 PHASE 1 – TIRAGE :\n" +
                        "• Une fille est tirée au sort (fille principale) devant qui les garçons font leur speech\n" +
                        "• Un garçon est tiré au sort par équipe\n" +
                        "• Si la fille principale est dans une équipe qui a aussi un garçon : ce garçon performe devant une 2e fille (tirée au sort)\n\n" +
                        "⚔️ PHASE 2 – PASSAGES :\n" +
                        "• Chaque garçon fait son speech (1-2 min) devant sa fille désignée\n" +
                        "• La fille (ou le jury) note ou vote\n" +
                        "• On sélectionne 2 finalistes\n\n" +
                        "🏆 PHASE 3 – FINALE :\n" +
                        "• Les 2 finalistes font chacun un speech devant la même fille (ou les filles)\n" +
                        "• La fille ou le jury désigne le grand gagnant\n\n" +
                        "📝 L'app vous guide pour les tirages et enregistre le classement des équipes.",
                        4, Game.GameType.REPRESENTANT),

                new Game("Gage",
                        "🎲 RÈGLES DU JEU DE GAGE\n\n" +
                        "🎯 CONCEPT :\n" +
                        "• L'équipe dernière du classement actuel est sélectionnée\n" +
                        "• Un membre de cette équipe est tiré au sort\n" +
                        "• Il doit réaliser un gage pour faire gagner des points à son équipe\n\n" +
                        "📋 DÉROULEMENT :\n" +
                        "• 3 gages proposés : 🟢 Vert (+2 pts), 🟠 Orange (+3 pts), 🔴 Rouge (+5 pts)\n" +
                        "• Le joueur choisit ; s'il accomplit le gage → l'équipe gagne les points\n" +
                        "• S'il refuse → tirage d'un autre membre (exclu) ; si tous refusent → 0 point\n\n" +
                        "💡 Consentement requis pour les actions impliquant d'autres personnes.\n\n" +
                        "📝 Indiquez l'équipe et si le gage a été accompli (et lequel).",
                        5, Game.GameType.TOUS_ENSEMBLE)
        );

        gameRepository.saveAll(games);
        logger.info("✅ {} jeux initialisés (soirée anniversaire)", games.size());
        for (Game game : games) {
            logger.info("   📋 Jeu #{}: {} ({})", game.getOrder(), game.getName(), game.getType());
        }
        logger.info("═══════════════════════════════════════════════════════════");
    }

    /**
     * Récupérer tous les jeux dans l'ordre
     */
    public List<Game> getAllGames() {
        return gameRepository.findAllByOrderByOrderAsc();
    }

    /**
     * Enregistrer les résultats d'un jeu
     * positions : Map<teamId, position> où position = 1, 2, 3, ou 4
     */
    @Transactional
    public void saveGameResults(Long gameId, Map<Long, Integer> positions) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Jeu non trouvé"));

        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        logger.info("🎯 ENREGISTREMENT DES RÉSULTATS - Jeu: {} (ID: {})", game.getName(), gameId);
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // VALIDATION : Vérifier que toutes les équipes ont une position
        List<Team> allTeams = teamRepository.findAll();
        if (positions.size() != allTeams.size()) {
            throw new IllegalArgumentException("Toutes les équipes doivent avoir une position assignée");
        }

        // VALIDATION : Vérifier que toutes les positions sont uniques
        long uniquePositions = positions.values().stream().distinct().count();
        if (uniquePositions != positions.size()) {
            throw new IllegalArgumentException("Chaque équipe doit avoir une position unique (pas de doublons)");
        }

        // VALIDATION : Vérifier que les positions sont valides (1 à nombre d'équipes)
        int maxPosition = allTeams.size();
        for (Map.Entry<Long, Integer> entry : positions.entrySet()) {
            int position = entry.getValue();
            if (position < 1 || position > maxPosition) {
                throw new IllegalArgumentException(
                    String.format("Position invalide: %d. Doit être entre 1 et %d", position, maxPosition)
                );
            }
        }

        logger.info("✅ Validations passées : {} équipes avec positions uniques", positions.size());

        // Marquer le jeu comme complété
        game.setCompleted(true);
        gameRepository.save(game);
        logger.info("✅ Jeu marqué comme complété");

        // Pour chaque équipe, créer un résultat
        positions.forEach((teamId, position) -> {
            Team team = teamRepository.findById(teamId)
                    .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));

            int pointsAvant = team.getTotalPoints();
            int shotsAvant = team.getShotsCount();

            // Créer le résultat
            GameResult result = new GameResult(game, team, position);
            gameResultRepository.save(result);

            // Mettre à jour les points de l'équipe
            team.addPoints(result.getPointsEarned());

            // Ajouter les shots selon la position (plus on est mal classé, plus on boit)
            // 1er = 0 shot, 2ème = 1 shot, 3ème = 2 shots, 4ème = 3 shots, etc.
            int shotsToAdd = result.getShotsCount();
            if (shotsToAdd > 0) {
                team.addShots(shotsToAdd);
                // Répartir aléatoirement les shots entre les joueurs de l'équipe (max 2 par joueur)
                distributeShotsToPlayers(team, shotsToAdd);
            }

            teamRepository.save(team);

            // Log détaillé pour chaque équipe
            String medal = switch(position) {
                case 1 -> "🥇";
                case 2 -> "🥈";
                case 3 -> "🥉";
                default -> "4️⃣";
            };
            
            int shotsAdded = team.getShotsCount() - shotsAvant;
            logger.info("   {} Position {}: {} → {} points (+{}), {} shots (+{})", 
                medal, position, team.getName(), 
                team.getTotalPoints(), result.getPointsEarned(),
                team.getShotsCount(), shotsAdded);
        });

        // Afficher le classement après ce jeu
        logger.info("📊 CLASSEMENT APRÈS CE JEU:");
        List<Team> teamsSorted = teamRepository.findAll();
        teamsSorted.sort((t1, t2) -> Integer.compare(t2.getTotalPoints(), t1.getTotalPoints()));
        for (int i = 0; i < teamsSorted.size(); i++) {
            Team t = teamsSorted.get(i);
            logger.info("   {}. {} - {} points - {} shots", i + 1, t.getName(), t.getTotalPoints(), t.getShotsCount());
        }
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Récupérer les résultats d'un jeu spécifique
     */
    public List<GameResult> getGameResults(Long gameId) {
        return gameResultRepository.findByGameId(gameId);
    }

    /**
     * Récupérer l'historique d'une équipe
     */
    public List<GameResult> getTeamHistory(Long teamId) {
        return gameResultRepository.findByTeamId(teamId);
    }

    /**
     * Enregistrer les points bonus d'un gage
     * Pour le jeu "Gage", on ajoute directement des points à l'équipe sans créer de classement
     */
    @Transactional
    public void saveGageBonus(Long gameId, Long teamId, int points) {
        logger.info("═══════════════════════════════════════════════════════════");
        logger.info("🎲 ENREGISTREMENT POINTS BONUS GAGE");
        logger.info("═══════════════════════════════════════════════════════════");
        
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Jeu non trouvé"));
        
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));
        
        int pointsAvant = team.getTotalPoints();
        
        // Ajouter les points bonus directement à l'équipe
        team.addPoints(points);
        teamRepository.save(team);
        
        // Marquer le jeu comme complété
        game.setCompleted(true);
        gameRepository.save(game);
        
        logger.info("✅ Points bonus ajoutés: {} → {} points (+{})", 
            team.getName(), team.getTotalPoints(), points);
        logger.info("═══════════════════════════════════════════════════════════");
    }

    /**
     * Enregistrer le résultat Whisky Undercover (Mister White).
     * Si éliminé : équipe Mister White 0 pt, les autres 2 pts chacune.
     * Si non éliminé : équipe Mister White 3 pts, les autres 0.
     */
    @Transactional
    public void saveUndercoverOutcome(Long gameId, Long misterWhiteTeamId, Boolean eliminated, Integer eliminatedInRound) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Jeu non trouvé"));
        List<Team> allTeams = teamRepository.findAll();
        Team misterWhiteTeam = teamRepository.findById(misterWhiteTeamId)
                .orElseThrow(() -> new IllegalArgumentException("Équipe Mister White non trouvée"));
        if (!allTeams.stream().anyMatch(t -> t.getId().equals(misterWhiteTeamId))) {
            throw new IllegalArgumentException("L'équipe Mister White doit faire partie des équipes de la soirée");
        }
        if (eliminated == null) {
            eliminated = false;
        }
        if (eliminated && (eliminatedInRound == null || eliminatedInRound < 1 || eliminatedInRound > 3)) {
            throw new IllegalArgumentException("Si Mister White est éliminé, indiquez le tour (1, 2 ou 3)");
        }

        game.setCompleted(true);
        gameRepository.save(game);

        List<Team> otherTeams = allTeams.stream().filter(t -> !t.getId().equals(misterWhiteTeamId)).toList();
        int pos = 1;
        if (Boolean.TRUE.equals(eliminated)) {
            // Mister White éliminé : son équipe 0 pt (position 4), les autres 2 pts chacune (positions 1,2,3)
            for (Team t : otherTeams) {
                GameResult r = new GameResult(game, t, pos, 2);
                gameResultRepository.save(r);
                t.addPoints(2);
                teamRepository.save(t);
                pos++;
            }
            GameResult rWhite = new GameResult(game, misterWhiteTeam, 4, 0);
            gameResultRepository.save(rWhite);
            logger.info("🕵️ Whisky Undercover: Mister White (équipe {}) éliminé au tour {} → 0 pt, les autres 2 pts", misterWhiteTeam.getName(), eliminatedInRound);
        } else {
            // Mister White survivant : son équipe 3 pts (1re), les autres 0 (2e, 3e, 4e)
            GameResult rWhite = new GameResult(game, misterWhiteTeam, 1, 3);
            gameResultRepository.save(rWhite);
            misterWhiteTeam.addPoints(3);
            teamRepository.save(misterWhiteTeam);
            pos = 2;
            for (Team t : otherTeams) {
                GameResult r = new GameResult(game, t, pos, 0);
                gameResultRepository.save(r);
                pos++;
            }
            logger.info("🕵️ Whisky Undercover: Mister White (équipe {}) n'a pas été éliminé → 3 pts, les autres 0", misterWhiteTeam.getName());
        }
    }

    /**
     * Répartit aléatoirement les shots entre les joueurs d'une équipe
     * Contrainte : maximum 2 shots par joueur
     * 
     * @param team L'équipe concernée
     * @param totalShots Le nombre total de shots à répartir
     */
    private void distributeShotsToPlayers(Team team, int totalShots) {
        List<Player> players = new ArrayList<>(team.getPlayers());
        
        if (players.isEmpty()) {
            logger.warn("⚠️  Équipe {} n'a pas de joueurs, les shots ne peuvent pas être répartis", team.getName());
            return;
        }

        // Mélanger aléatoirement la liste des joueurs pour une distribution aléatoire
        Collections.shuffle(players, new Random());

        int remainingShots = totalShots;
        int playerIndex = 0;

        logger.info("   🎲 Répartition aléatoire de {} shots pour l'équipe {}:", totalShots, team.getName());

        // Distribuer les shots en respectant la contrainte (max 2 par joueur)
        while (remainingShots > 0 && playerIndex < players.size()) {
            Player player = players.get(playerIndex);
            
            // Calculer combien de shots ce joueur peut recevoir (max 2, ou le reste s'il en reste moins)
            int shotsForThisPlayer = Math.min(2, remainingShots);
            
            player.addShots(shotsForThisPlayer);
            playerRepository.save(player); // Sauvegarder le joueur avec ses shots
            remainingShots -= shotsForThisPlayer;
            
            logger.info("      → {} : {} shot{}", player.getName(), shotsForThisPlayer, shotsForThisPlayer > 1 ? "s" : "");
            
            playerIndex++;
            
            // Si on a distribué tous les shots, on s'arrête
            if (remainingShots <= 0) {
                break;
            }
            
            // Si on a fait le tour de tous les joueurs et qu'il reste des shots,
            // on recommence depuis le début
            if (playerIndex >= players.size()) {
                playerIndex = 0;
            }
        }

        if (remainingShots > 0) {
            logger.warn("   ⚠️  Il reste {} shots non distribués (tous les joueurs ont déjà 2 shots)", remainingShots);
        }
    }
}