package tech.assignment.kalaha.service;

import tech.assignment.kalaha.model.Board;
import tech.assignment.kalaha.model.GameState;
import tech.assignment.kalaha.model.Player;

import java.util.List;

public class TestDataProvider {

    public static Board createFirstPlayerWonBoard() {
        Board board = new Board();
        board.setPlayer1Id(1L);
        board.setPlayer2Id(2L);
        board.setPool1(40);
        board.setPool2(32);
        for(int i=0; i<6; i++) {
            board.getSide1().add(0);
            board.getSide2().add(0);
        }
        board.setGameState(GameState.FIRST_PLAYER_WON);
        return board;
    }

    public static Board createCustomBoard(
            List<Integer> stones1,
            List<Integer> stones2,
            int pool1,
            int pool2
    ) {
        Board board = new Board();
        board.setPlayer1Id(1L);
        board.setPlayer2Id(2L);
        board.setPool1(pool1);
        board.setPool2(pool2);
        board.getSide1().addAll(stones1);
        board.getSide2().addAll(stones2);
        board.setGameState(GameState.ONGOING);
        return board;
    }

    public static Player createFirstPlayer() {
        Player player = new Player();
        player.setId(1L);
        player.setLogin("login1");
        player.setNickname("Alice");
        player.setWins(1L);
        return player;
    }

    public static Player createSecondPlayer() {
        Player player = new Player();
        player.setId(2L);
        player.setLogin("login2");
        player.setNickname("Bob");
        player.setLoses(1L);
        return player;
    }

    public static Player createHackerPlayer() {
        Player player = new Player();
        player.setId(777L);
        player.setLogin("root");
        player.setNickname("Hackerman");
        player.setWins(999L);
        player.setLoses(-1L);
        return player;
    }
}
