# 📋 Guide des Logs de l'Application Soirée Jeux

## 📁 Structure des Logs

L'application génère plusieurs fichiers de logs dans le répertoire `logs/` :

1. **`soiree-jeux.log`** : Log principal de l'application (toutes les opérations)
2. **`game-evolution.log`** : Log dédié à l'évolution des jeux et des scores

### Rotation des Logs

Les logs sont archivés quotidiennement :
- Format : `soiree-jeux-YYYY-MM-DD.log`
- Format : `game-evolution-YYYY-MM-DD.log`

## 📊 Log game-evolution.log

Ce fichier contient une trace détaillée de l'évolution des scores et des jeux.

### Exemple de contenu

```
2025-12-30 02:04:55.700 | 🎮 INITIALISATION DE LA SOIRÉE
2025-12-30 02:04:55.701 | 📋 Jeu #1: Blindtest (TOUS_ENSEMBLE)
2025-12-30 02:04:55.702 | 📋 Jeu #2: Jeu de Mime (REPRESENTANT)
...
2025-12-30 02:04:55.710 | 👥 ÉQUIPES CRÉÉES:
2025-12-30 02:04:55.711 |    🎨 Équipe 1 (#FF6B6B): 5 joueurs (3H/2F) - Points: 0 - Shots: 0
...
🎯 ENREGISTREMENT DES RÉSULTATS - Jeu: Blindtest (ID: 1)
   🥇 Position 1: Équipe 2 → 3 points (+3), 0 shots (+0)
   🥈 Position 2: Équipe 4 → 5 points (+2), 1 shots (+1)
   🥉 Position 3: Équipe 3 → 3 points (+1), 2 shots (+2)
   4️⃣ Position 4: Équipe 1 → 1 points (+0), 3 shots (+3)
📊 CLASSEMENT APRÈS CE JEU:
   1. Équipe 4 - 5 points - 1 shots
   2. Équipe 2 - 3 points - 0 shots
   3. Équipe 3 - 3 points - 2 shots
   4. Équipe 1 - 1 points - 3 shots
```

## ✅ Vérification de la Logique

### Points
- ✅ Vérifiez que les points sont attribués correctement :
  - 1er = +3 points
  - 2ème = +2 points
  - 3ème = +1 point
  - 4ème = +0 point

### Shots
- ✅ Vérifiez que les shots sont attribués correctement :
  - 1er = 0 shot
  - 2ème = 1 shot
  - 3ème = 2 shots
  - 4ème = 3 shots

## 🔍 Comment Analyser les Logs

1. **Vérifier l'initialisation** : Recherchez `🎮 INITIALISATION DE LA SOIRÉE`
2. **Suivre l'évolution des points** : Recherchez `📊 CLASSEMENT APRÈS CE JEU`
3. **Vérifier les résultats d'un jeu** : Recherchez `🎯 ENREGISTREMENT DES RÉSULTATS`
4. **Analyser les erreurs** : Recherchez `❌` ou `ERROR`

## 📝 Format des Messages

- **🎮** : Événements de jeu
- **📋** : Informations sur les jeux
- **👥** : Informations sur les équipes
- **🎯** : Résultats des jeux
- **📊** : Classements
- **✅** : Succès
- **❌** : Erreurs

## 🛠️ Nettoyage des Logs

Les anciens logs sont automatiquement archivés. Pour nettoyer manuellement :

```bash
# Supprimer les logs archivés de plus de 30 jours
find logs/ -name "*.log" -mtime +30 -delete
```
