package tech.assignment.kalaha.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.assignment.kalaha.model.Board;
import tech.assignment.kalaha.service.GameService;
import tech.assignment.kalaha.web.dto.BoardDto;

@RestController
@RequestMapping("/api/game")
@ApiOperation(value = "Game API", notes = "Provides methods to control one Game match")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @ApiOperation(value = "Get a game by id", notes = "Returns a current state of the Game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not found - The game does not exist")
    })
    @GetMapping("/{gameId}")
    public BoardDto getGame(
            @ApiParam(name = "gameId", value = "Game id to load", example = "1")
            @PathVariable Long gameId,
            @ApiParam(name = "playerId", value = "Player id, owner of the Game", example = "12")
            @RequestParam(required = false) Long playerId
    ) {
        Board board = gameService.getBoard(gameId);
        return transform(board, playerId);
    }

    @ApiOperation(value = "Create a game", notes = "Creates a new Game for a given Player")
    @PostMapping
    public BoardDto createGame(
            @ApiParam(name = "playerId", value = "Player id, owner of the Game", example = "12")
            @RequestParam Long playerId,
            @ApiParam(name = "size", value = "Number of pits for each player (optional)", example = "3")
            @RequestParam(required = false) Integer size,
            @ApiParam(name = "stones", value = "Number of stones in each pit (optional)", example = "2")
            @RequestParam(required = false) Integer stones
    ){
        if (size == null || stones == null) {
            Board board = gameService.createGame(playerId);
            return transform(board, playerId);
        } else {
            Board board = gameService.createCustomGame(playerId, size, stones);
            return transform(board, playerId);
        }
    }

    @ApiOperation(value = "Join the game", notes = "Appends second Player to the Game")
    @PutMapping("/{gameId}/join")
    public BoardDto joinGame(
            @ApiParam(name = "gameId", value = "Game id", example = "1")
            @PathVariable Long gameId,
            @ApiParam(name = "playerId", value = "Player id, who makes the move", example = "2")
            @RequestParam Long playerId
    ) {
        Board board = gameService.joinGame(gameId, playerId);
        return transform(board, playerId);
    }

    @ApiOperation(value = "Make a move", notes = "Given Player makes a move in the Game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Wrong move, see Response message"),
            @ApiResponse(code = 404, message = "Not found - The game does not exist")
    })
    @PostMapping("/{gameId}/move")
    public BoardDto makeMove(
            @ApiParam(name = "gameId", value = "Game id", example = "1")
            @PathVariable Long gameId,
            @ApiParam(name = "playerId", value = "Player id, who makes the move", example = "2")
            @RequestParam Long playerId,
            @ApiParam(name = "move", value = "Pit number, where turn starts", example = "0")
            @RequestParam Integer move
    ) {
        Board board = gameService.makeMove(gameId, playerId, move);
        return transform(board, playerId);
    }

    private BoardDto transform(Board board, Long playerId) {
        var mySide = detectSide(board, playerId);
        return new BoardDto(
                board.getId(),
                board.getName(),
                board.getSide1(),
                board.getSide2(),
                board.getPool1(),
                board.getPool2(),
                board.getGameStatus(),
                mySide,
                board.getTurn() == mySide
        );
    }

    private int detectSide(Board board, Long playerId) {
        if (playerId == null) {
            return 0;
        } else if (playerId.equals(board.getPlayer1Id())) {
            return 1;
        } else if (playerId.equals(board.getPlayer2Id())) {
            return 2;
        } else {
            return 0;
        }
    }
}
