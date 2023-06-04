package tech.assignment.kalaha.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.assignment.kalaha.model.Board;
import tech.assignment.kalaha.model.GameState;
import tech.assignment.kalaha.repository.BoardRepository;
import tech.assignment.kalaha.service.GameService;
import tech.assignment.kalaha.service.PlayerService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class GameCrudTest {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameService gameService;

    @Test
    void gameCreationTest() {
        Long playerId = playerService.createPlayer("login_1", "Jon Snow").getId();

        Board created = gameService.createGame(playerId);
        Board retrieved = gameService.getBoard(created.getId());
        Board saved = boardRepository.findById(created.getId()).orElseThrow();

        assertEquals(playerId, created.getPlayer1Id());
        assertEquals(playerId, retrieved.getPlayer1Id());
        assertEquals(playerId, saved.getPlayer1Id());
        assertNull(created.getPlayer2Id());
        assertNull(retrieved.getPlayer2Id());
        assertNull(saved.getPlayer2Id());
        assertEquals(0, created.getPool1());
        assertEquals(0, retrieved.getPool1());
        assertEquals(0, saved.getPool1());
        assertEquals(0, created.getPool2());
        assertEquals(0, retrieved.getPool2());
        assertEquals(0, saved.getPool2());
        assertEquals(created.getSide1(), retrieved.getSide1());
        assertEquals(created.getSide1(), saved.getSide1());
        assertEquals(created.getSide2(), retrieved.getSide2());
        assertEquals(created.getSide2(), saved.getSide2());
    }

    @Test
    void existingGameRetrievalTest() {
        Long player1Id = 42L;
        Long player2Id = 50L;
        int pool1 = 5;
        int pool2 = 3;
        int turn = 2;
        GameState state = GameState.ONGOING;

        Board board = new Board();
        board.setPlayer1Id(player1Id);
        board.setPlayer2Id(player2Id);
        board.setPool1(pool1);
        board.setPool2(pool2);
        board.getSide1().add(4);
        board.getSide2().add(2);
        board.setGameState(state);
        board.setTurn(turn);

        Long boardId = boardRepository.save(board).getId();
        Board retrieved = gameService.getBoard(boardId);

        assertEquals(player1Id, retrieved.getPlayer1Id());
        assertEquals(player2Id, retrieved.getPlayer2Id());
        assertEquals(pool1, retrieved.getPool1());
        assertEquals(pool2, retrieved.getPool2());
        assertEquals(state, retrieved.getGameState());
        assertEquals(turn, retrieved.getTurn());
        assertEquals(board.getSide1(), retrieved.getSide1());
        assertEquals(board.getSide2(), retrieved.getSide2());
    }
}
