# Kalaha multiplayer game

Game of Kalaha. This is server-based online multiplayer board game for two players.
Supports spectator mode and a leaderboard.

### Rules
Board Setup  
Each of the two players has his six pits in front of him. To the right of the six pits,
each player has a larger pit. At the start of the game, there are six stones in each
of the six round pits.

Game Play  
The player who begins with the first move picks up all the stones in any of his own
six pits, and sows the stones on to the right, one in each of the following pits,
including his own big pit. No stones are put in the opponents' big pit. If the player's
last stone lands in his own big pit, he gets another turn. This can be repeated
several times before it's the other player's turn.

Capturing Stones  
During the game the pits are emptied on both sides. Always when the last stone
lands in an own empty pit, the player captures his own stone and all stones in the
opposite pit (the other player’s pit) and puts them in his own (big or little?) pit.

The Game Ends  
The game is over as soon as one of the sides runs out of stones. The player who
still has stones in his pits keeps them and puts them in his big pit. The winner of
the game is the player who has the most stones in his big pit.

### How to build & run
1. Build project into `build/lib` (default location)
1. Build Docker image(s):
   ```
   docker-compose build
   ```
1. Run docker compose:
   ```
   docker-compose up
   ```
1. Open the game in a browser:
   ```
   http://localhost:8080/front/index.html
   ```
   Open swagger:
   ```
   http://localhost:8080/swagger-ui/
   ```

### Implementation details
1. Kalaha engine (with ability to create custom boards).
1. Back-end microservice to create, join and play a game.
1. Tests (Unit tests + Integration tests).
1. Actuator for metrics.
1. Swagger for API docs.
1. Dockerfile and docker-compose for containerization.
1. Simple front-end with leaderboard and spectator mode.

### TODO
List of improvements that could be done, but were not applied yet:
1. Split front-end and back-end parts. These should be different repos for production-like solution.
1. Append moves History for each Game and ability to Replay it.
1. AI player.
1. Improve "join game" procedure on front-end: Generate a full link to join instead of "id" field.
1. Replace Long with UUID for playerId and gameId, if we want more security here.
1. We suppose this is a microservice BEHIND an Auth service, which provides all the security and identification functionality. If we suppose to make this project a standalone web app, we should append security, proper identification and login/password functionality alongside UUIDs for the entities ids.
1. Append Prometeus+Grafana metrics. We have Actuator endpoints only for now.
