package tech.assignment.kalaha.web.dto;

import io.swagger.annotations.ApiModelProperty;
import tech.assignment.kalaha.model.GameState;

import java.util.ArrayList;
import java.util.List;

public class BoardDto {

    @ApiModelProperty(notes = "Game id", example = "10", required = true)
    private Long id;
    @ApiModelProperty(notes = "Pits for Player 1, contains amount of stones in each", example = "[1, 2, 3]", required = true)
    private List<Integer> pits1 = new ArrayList<>();
    @ApiModelProperty(notes = "Pits for Player 2, contains amount of stones in each", example = "[4, 4, 4]", required = true)
    private List<Integer> pits2 = new ArrayList<>();
    @ApiModelProperty(notes = "Amount of captured stones in the pool (big pit) of the Player 1", example = "6", required = true)
    private int pool1;
    @ApiModelProperty(notes = "Amount of captured stones in the pool (big pit) of the Player 2", example = "0", required = true)
    private int pool2;
    @ApiModelProperty(notes = "Current game state", example = "ONGOING", allowableValues = "ONGOING, FIRST_PLAYER_WON, SECOND_PLAYER_WON, DRAW", required = true)
    private GameState gameState = GameState.ONGOING;

    public BoardDto(
            Long id,
            List<Integer> side1,
            List<Integer> side2,
            int pool1,
            int pool2,
            GameState gameState
    ) {
        this.id = id;
        this.pool1 = pool1;
        this.pool2 = pool2;
        this.gameState = gameState;
        this.pits1.addAll(side1);
        this.pits2.addAll(side2);
    }
}
