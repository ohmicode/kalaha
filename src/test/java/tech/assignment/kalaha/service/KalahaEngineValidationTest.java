package tech.assignment.kalaha.service;

import org.junit.jupiter.api.Test;
import tech.assignment.kalaha.exception.EmptyFieldException;
import tech.assignment.kalaha.exception.GameStateException;
import tech.assignment.kalaha.exception.WrongPlayerException;
import tech.assignment.kalaha.model.Board;
import tech.assignment.kalaha.model.Player;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static tech.assignment.kalaha.service.TestDataProvider.*;

public class KalahaEngineValidationTest {

    private KalahaEngine engine = new KalahaEngine();

    @Test
    void shouldCheckEndOfGame() {
        Board board = createFirstPlayerWonBoard();
        Player player = createFirstPlayer();
        int move = 1;

        assertThrows(GameStateException.class, () -> {
            engine.makeMove(board, player, move);
        });
    }

    @Test
    void shouldCheckGameIsStarted() {
        Player player1 = createFirstPlayer();
        Board board = engine.createGame(player1);
        int move = 0;

        assertThrows(GameStateException.class, () -> {
            engine.makeMove(board, player1, move);
        });
    }

    @Test
    void shouldCheckPlayer() {
        Player player1 = createFirstPlayer();
        Player player2 = createSecondPlayer();
        Player hacker = createHackerPlayer();
        Board board = engine.createGame(player1);
        board.setPlayer2Id(player2.getId());
        int move = 7;

        assertThrows(WrongPlayerException.class, () -> {
            engine.makeMove(board, hacker, move);
        });
    }

    @Test
    void shouldCheckTurnOrder() {
        Player player1 = createFirstPlayer();
        Player player2 = createSecondPlayer();
        Board board = engine.createGame(player1);
        board.setPlayer2Id(player2.getId());
        int move = 0;

        assertThrows(WrongPlayerException.class, () -> {
            engine.makeMove(board, player2, move);
        });
    }

    @Test
    void shouldCheckEmptyTurn() {
        Player player1 = createFirstPlayer();
        Player player2 = createSecondPlayer();
        Board board = engine.createGame(player1);
        board.setPlayer2Id(player2.getId());
        board.getSide1().set(0, 0);
        int move = 0;

        assertThrows(EmptyFieldException.class, () -> {
            engine.makeMove(board, player1, move);
        });
    }
}
