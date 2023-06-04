package tech.assignment.kalaha.service;

import org.junit.jupiter.api.Test;
import tech.assignment.kalaha.model.Board;
import tech.assignment.kalaha.model.GameState;
import tech.assignment.kalaha.model.Player;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.assignment.kalaha.service.TestDataProvider.*;

public class KalahaEngineExecutionTest {

    private KalahaEngine engine = new KalahaEngine();

    @Test
    void repeatTurn() {
        Board board = createCustomBoard(
                Arrays.asList(1, 1, 1, 1),
                Arrays.asList(1, 1, 1, 1),
                0, 0
        );
        Player player = createFirstPlayer();
        int move = 3;

        Board result = engine.makeMove(board, player, move);

        assertEquals(GameState.ONGOING, result.getGameState());
        assertEquals(0, result.getSide1().get(move));
        assertEquals(1, result.getPool1());
        assertEquals(1, result.getTurn());
    }

    @Test
    void switchTurn() {
        Board board = createCustomBoard(
                Arrays.asList(2, 2, 2, 2),
                Arrays.asList(2, 2, 2, 2),
                0, 0
        );
        Player player = createFirstPlayer();
        int move = 0;

        Board result = engine.makeMove(board, player, move);

        assertEquals(GameState.ONGOING, result.getGameState());
        assertEquals(0, result.getSide1().get(move));
        assertEquals(0, result.getPool1());
        assertEquals(0, result.getPool2());
        assertEquals(2, result.getTurn());
    }

    @Test
    void captureOppositeStones() {
        Board board = createCustomBoard(
                Arrays.asList(2, 1, 0, 2),
                Arrays.asList(3, 4, 3, 3),
                5, 7
        );
        Player player = createFirstPlayer();
        int move = 1;

        Board result = engine.makeMove(board, player, move);

        assertEquals(GameState.ONGOING, result.getGameState());
        assertEquals(0, result.getSide1().get(move));
        assertEquals(0, result.getSide1().get(move + 1));
        assertEquals(10, result.getPool1());
        assertEquals(7, result.getPool2());
        assertEquals(2, result.getTurn());
    }

    @Test
    void lastTurn() {
        Board board = createCustomBoard(
                Arrays.asList(0, 0, 0, 1),
                Arrays.asList(1, 2, 3, 4),
                5, 5
        );
        Player player = createFirstPlayer();
        int move = 3;

        Board result = engine.makeMove(board, player, move);

        assertEquals(GameState.SECOND_PLAYER_WON, result.getGameState());
        assertEquals(0, result.getSide1().get(move));
        assertEquals(6, result.getPool1());
        assertEquals(15, result.getPool2());
    }

    @Test
    void middleGame4Turns() {
        Board board = createCustomBoard(
                Arrays.asList(4, 4, 4),
                Arrays.asList(4, 4, 4),
                0, 0
        );
        Player player1 = createFirstPlayer();
        Player player2 = createSecondPlayer();

        Board result = engine.makeMove(board, player1, 2);
        result = engine.makeMove(result, player2, 2);
        result = engine.makeMove(result, player1, 2);
        result = engine.makeMove(result, player1, 0);

        assertEquals(GameState.ONGOING, result.getGameState());
        assertEquals(Arrays.asList(0, 6, 1), result.getSide1());
        assertEquals(Arrays.asList(7, 6, 0), result.getSide2());
        assertEquals(3, result.getPool1());
        assertEquals(1, result.getPool2());
    }

    @Test
    void fullGameShort() {
        Board board = createCustomBoard(
                Arrays.asList(1, 1),
                Arrays.asList(1, 1),
                0, 0
        );
        Player player1 = createFirstPlayer();

        Board result = engine.makeMove(board, player1, 1);
        result = engine.makeMove(result, player1, 0);

        assertEquals(GameState.FIRST_PLAYER_WON, result.getGameState());
        assertEquals(Arrays.asList(0, 0), result.getSide1());
        assertEquals(Arrays.asList(0, 0), result.getSide2());
        assertEquals(3, result.getPool1());
        assertEquals(1, result.getPool2());
    }

    @Test
    void fullGameDraw() {
        Board board = createCustomBoard(
                Arrays.asList(1, 1, 1),
                Arrays.asList(1, 1, 1),
                0, 0
        );
        Player player1 = createFirstPlayer();
        Player player2 = createSecondPlayer();

        Board result = engine.makeMove(board, player1, 2);
        result = engine.makeMove(result, player1, 0);
        result = engine.makeMove(result, player2, 0);
        result = engine.makeMove(result, player1, 1);
        result = engine.makeMove(result, player1, 2);

        assertEquals(GameState.DRAW, result.getGameState());
        assertEquals(Arrays.asList(0, 0, 0), result.getSide1());
        assertEquals(Arrays.asList(0, 0, 0), result.getSide2());
        assertEquals(3, result.getPool1());
        assertEquals(3, result.getPool2());
    }
}
