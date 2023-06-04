package tech.assignment.kalaha.service;

import org.springframework.stereotype.Service;
import tech.assignment.kalaha.exception.EmptyFieldException;
import tech.assignment.kalaha.exception.GameStateException;
import tech.assignment.kalaha.exception.WrongPlayerException;
import tech.assignment.kalaha.model.Board;
import tech.assignment.kalaha.model.GameState;
import tech.assignment.kalaha.model.Player;

import java.util.List;

@Service
public class KalahaEngine {
    private static final int DEFAULT_BOARD_SIZE = 6;
    private static final int DEFAULT_AMOUNT = 6;

    /**
     * Creates a board with default settings (6 pits per player, 6 stones each).
     * @param player1 - Owner of the game (player #1)
     * @return initialised Board
     */
    public Board createGame(Player player1) {
        Board board = initBoard(DEFAULT_BOARD_SIZE, DEFAULT_AMOUNT);
        board.setPlayer1Id(player1.getId());
        board.setName(player1.getNickname() + " - ");
        return board;
    }

    /**
     * Creates a board with custom settings
     * @param player1 - Owner of the game (player #1)
     * @param size - Number of pits on each side
     * @param startAmount - Number of stones in each pit
     * @return initialised Board
     */
    public Board createCustomGame(Player player1, int size, int startAmount) {
        Board board = initBoard(size, startAmount);
        board.setPlayer1Id(player1.getId());
        board.setName(player1.getNickname() + " - ");
        return board;
    }

    private Board initBoard(int size, int startAmount) {
        Board board = new Board();
        for(int i=0; i<size; i++) {
            board.getSide1().add(startAmount);
            board.getSide2().add(startAmount);
        }
        return board;
    }

    /**
     * Tries to make a move on the Board for the given Player.
     * Method validates an ability to make that move and executes if possible,
     * returning the new state.
     * @param board - current state
     * @param player - who makes the move
     * @param pitNumber - number of pit on Player's side, starting 0 (to 5 for default settings)
     * @throws GameStateException, WrongPlayerException, EmptyFieldException for illegal moves
     * @return new Board state
     */
    public Board makeMove(Board board, Player player, int pitNumber) {
        validate(board, player, pitNumber);
        TurnType turnType = moveStones(board, pitNumber);
        if (turnType == TurnType.SIDE) {
            switchTurn(board);
        }
        if (isGameEnded(board)) {
            calculateResult(board);
        }
        return board;
    }

    private void validate(Board board, Player player, int pitNumber) {
        if (board.getGameState() != GameState.ONGOING) {
            throw new GameStateException("This game has been finished");
        }
        if (board.getTurn() == 1) {
            if (!player.getId().equals(board.getPlayer1Id())) {
                throw new WrongPlayerException("It is not " + player.getNickname() + "'s turn");
            }
            if (board.getSide1().get(pitNumber).equals(0)) {
                throw new EmptyFieldException("Pit " + pitNumber + " is empty");
            }
        } else {
            if (!player.getId().equals(board.getPlayer2Id())) {
                throw new WrongPlayerException("It is not " + player.getNickname() + "'s turn");
            }
            if (board.getSide2().get(pitNumber).equals(0)) {
                throw new EmptyFieldException("Pit " + pitNumber + " is empty");
            }
        }
    }

    private TurnType moveStones(Board board, int pitNumber) {
        List<Integer> mySide = board.getTurn() == 1 ? board.getSide1() : board.getSide2();
        List<Integer> opponentSide = board.getTurn() == 1 ? board.getSide2() : board.getSide1();

        Hand hand = new Hand(mySide.get(pitNumber), pitNumber + 1);
        mySide.set(pitNumber, 0);

        while (hand.stones > 0) {
            hand = putStones(mySide, hand, hand.position);
            if (isFinishedAtEmpty(mySide, hand)) {
                captureEnemyStones(board, mySide, opponentSide, hand);
            }
            hand = putIntoPool(board, hand);
            hand = putStones(opponentSide, hand, 0);
            hand.position = 0;
        }
        return hand.type;
    }

    private Hand putStones(List<Integer> oneSide, Hand hand, int startPosition) {
        int pos = startPosition;
        while (hand.stones > 0 && pos < oneSide.size()) {
            oneSide.set(pos, oneSide.get(pos) + 1);
            hand.stones--;
            hand.position = pos;
            pos++;
        }
        return hand;
    }

    private Hand putIntoPool(Board board, Hand hand) {
        if (hand.stones > 0) {
            if (board.getTurn() == 1) {
                board.setPool1(board.getPool1() + 1);
            } else {
                board.setPool2(board.getPool2() + 1);
            }
            hand.stones--;
            if (hand.stones == 0) {
                hand.type = TurnType.POOL;
            }
        }
        return hand;
    }

    private boolean isFinishedAtEmpty(List<Integer> mySide, Hand hand) {
        return hand.stones == 0 && mySide.get(hand.position) == 1;
    }

    private void captureEnemyStones(Board board, List<Integer> mySide, List<Integer> otherSide, Hand hand) {
        int otherPos = otherSide.size() - 1 - hand.position;
        int total = mySide.get(hand.position) + otherSide.get(otherPos);

        if (board.getTurn() == 1) {
            board.setPool1(board.getPool1() + total);
        } else {
            board.setPool2(board.getPool2() + total);
        }

        mySide.set(hand.position, 0);
        otherSide.set(otherPos, 0);
    }

    private void switchTurn(Board board) {
        board.setTurn(board.getTurn() == 1 ? 2 : 1);
    }

    private boolean isGameEnded(Board board) {
        int total1 = board.getSide1().stream().mapToInt(Integer::intValue).sum();
        int total2 = board.getSide2().stream().mapToInt(Integer::intValue).sum();
        return total1 == 0 || total2 == 0;
    }

    private void calculateResult(Board board) {
        int total1 = board.getSide1().stream().mapToInt(Integer::intValue).sum();
        int total2 = board.getSide2().stream().mapToInt(Integer::intValue).sum();
        board.setPool1(board.getPool1() + total1);
        board.setPool2(board.getPool2() + total2);
        for(int i=0; i<board.getSide1().size(); i++) {
            board.getSide1().set(i, 0);
            board.getSide2().set(i, 0);
        }

        if (board.getPool1() > board.getPool2()) {
            board.setGameState(GameState.FIRST_PLAYER_WON);
        } else if (board.getPool2() > board.getPool1()) {
            board.setGameState(GameState.SECOND_PLAYER_WON);
        } else {
            board.setGameState(GameState.DRAW);
        }
    }

    enum TurnType { SIDE, POOL }

    static class Hand {
        int stones;
        int position;
        TurnType type = TurnType.SIDE;

        public Hand(int stones, int pos) {
            this.stones = stones;
            this.position = pos;
        }
    }
}
