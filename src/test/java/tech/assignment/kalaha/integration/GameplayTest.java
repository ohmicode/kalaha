package tech.assignment.kalaha.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.assignment.kalaha.model.Board;
import tech.assignment.kalaha.model.GameStatus;
import tech.assignment.kalaha.model.Player;
import tech.assignment.kalaha.service.GameService;
import tech.assignment.kalaha.service.PlayerService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GameplayTest {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameService gameService;

    @Test
    void shortGameFirstPlayerWin() {
        Long player1Id = playerService.createPlayer("player1", "Wilhelm Steinitz").getId();
        Long player2Id = playerService.createPlayer("player2", "Emanuel Lasker").getId();

        Long gameId = gameService.createCustomGame(player1Id, 2, 1).getId();
        gameService.joinGame(gameId, player2Id);

        gameService.makeMove(gameId, player1Id, 1);
        gameService.makeMove(gameId, player1Id, 0);

        Board game = gameService.getBoard(gameId);
        Player player1 = playerService.getPlayer(player1Id);
        Player player2 = playerService.getPlayer(player2Id);

        assertEquals(GameStatus.FIRST_PLAYER_WON, game.getGameStatus());
        assertEquals(3, game.getPool1());
        assertEquals(1, game.getPool2());
        assertEquals(1, player1.getWins());
        assertEquals(0, player1.getLoses());
        assertEquals(0, player2.getWins());
        assertEquals(1, player2.getLoses());
    }

    @Test
    void shortGameSecondPlayerWin() {
        Long player1Id = playerService.createPlayer("master1", "José Raúl Capablanca").getId();
        Long player2Id = playerService.createPlayer("master2", "Alexander Alekhine").getId();

        Long gameId = gameService.createCustomGame(player1Id, 3, 2).getId();
        gameService.joinGame(gameId, player2Id);

        gameService.makeMove(gameId, player1Id, 1);
        gameService.makeMove(gameId, player1Id, 0);
        gameService.makeMove(gameId, player2Id, 0);
        gameService.makeMove(gameId, player1Id, 2);
        gameService.makeMove(gameId, player2Id, 0);
        gameService.makeMove(gameId, player1Id, 1);

        Board game = gameService.getBoard(gameId);
        Player player1 = playerService.getPlayer(player1Id);
        Player player2 = playerService.getPlayer(player2Id);

        assertEquals(GameStatus.SECOND_PLAYER_WON, game.getGameStatus());
        assertEquals(3, game.getPool1());
        assertEquals(9, game.getPool2());
        assertEquals(0, player1.getWins());
        assertEquals(1, player1.getLoses());
        assertEquals(1, player2.getWins());
        assertEquals(0, player2.getLoses());
    }
}
