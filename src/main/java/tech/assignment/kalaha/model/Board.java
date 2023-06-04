package tech.assignment.kalaha.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long Player1Id;
    private Long Player2Id;
    private int turn = 1;
    @Type(type = "tech.assignment.kalaha.model.convert.JsonArrayType")
    private List<Integer> side1 = new ArrayList<>();
    @Type(type = "tech.assignment.kalaha.model.convert.JsonArrayType")
    private List<Integer> side2 = new ArrayList<>();
    private int pool1 = 0;
    private int pool2 = 0;
    private GameState gameState = GameState.ONGOING;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayer1Id() {
        return Player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        Player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return Player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        Player2Id = player2Id;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<Integer> getSide1() {
        return side1;
    }

    public void setSide1(List<Integer> side1) {
        this.side1 = side1;
    }

    public List<Integer> getSide2() {
        return side2;
    }

    public void setSide2(List<Integer> side2) {
        this.side2 = side2;
    }

    public int getPool1() {
        return pool1;
    }

    public void setPool1(int pool1) {
        this.pool1 = pool1;
    }

    public int getPool2() {
        return pool2;
    }

    public void setPool2(int pool2) {
        this.pool2 = pool2;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
