package tech.assignment.kalaha.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.assignment.kalaha.model.Player;
import tech.assignment.kalaha.service.PlayerService;
import tech.assignment.kalaha.web.dto.PlayerDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/player")
@ApiOperation(value = "Player API", notes = "Provides methods to create and retrieve a Player info")
public class PlayerController {

    private static final Logger logger = LogManager.getLogger(PlayerController.class.getName());

    private PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @ApiOperation(value = "Get a leaderboard", notes = "Returns list of 10 best players according to their win and lose statistics")
    @GetMapping("/leaderboard")
    public List<PlayerDto> getAllPlayers() {
        return playerService.getLeaderboard().stream().map(this::transform).collect(Collectors.toList());
    }

    @ApiOperation(value = "Get a player by id", notes = "Returns a Player info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not found - The player does not exist")
    })
    @GetMapping("/{playerId}")
    public PlayerDto getPlayer(
            @ApiParam(name = "playerId", value = "Player id to load", example = "1")
            @PathVariable Long playerId
    ) {
        Player player = playerService.getPlayer(playerId);
        return transform(player);
    }

    @ApiOperation(value = "Get a player by login", notes = "Finds a Player by login and returns the info")
    @GetMapping("/login")
    public PlayerDto login(
            @ApiParam(name = "login", value = "Player login", example = "user")
            @RequestParam String login
    ) {
        Player player = playerService.findByLogin(login);
        logger.debug("Player logged in: " + player.getId());
        return transform(player);
    }

    @ApiOperation(value = "Create a player", notes = "Creates a new Player if given login does not exist")
    @PostMapping
    public PlayerDto createPlayer(
            @ApiParam(name = "login", value = "User login", example = "noob")
            @RequestParam String login,
            @ApiParam(name = "nickname", value = "Displayed name", example = "Smart AI")
            @RequestParam String nickname
    ) {
        Player player = playerService.createPlayer(login, nickname);
        logger.info("New player created: " + player.getId());
        return transform(player);
    }

    private PlayerDto transform(Player player) {
        return new PlayerDto(
                player.getId(),
                player.getLogin(),
                player.getNickname(),
                player.getWins(),
                player.getLoses()
        );
    }
}
