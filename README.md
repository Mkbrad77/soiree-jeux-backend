# Soirée Jeux – Backend

API REST Spring Boot pour l’application de gestion d’une soirée jeux (équipes, jeux, classement, résultats).

## Stack

- Java 17, Spring Boot 4, Maven  
- JPA / Hibernate  
- Base dev : H2 en mémoire ; prod : PostgreSQL (Neon)

## Lancer en local

```bash
./mvnw spring-boot:run
```

- API : http://localhost:8080/api  
- Console H2 : http://localhost:8080/h2-console  

## Endpoints principaux

- **Équipes** : `GET/POST /api/teams`, `POST /api/teams/create`, `POST /api/teams/{id}/players`, `POST /api/teams/reset`
- **Jeux** : `POST /api/games/initialize`, `GET /api/games`, `POST /api/games/{id}/results`, `POST /api/games/{id}/gage-bonus`, `POST /api/games/{id}/undercover-outcome`

## Déploiement (Render)

Le profil `prod` utilise les variables d’environnement `DATABASE_URL` et `FRONTEND_URL`. Voir `render.yaml` et `src/main/resources/application-prod.properties`.
