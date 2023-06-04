package tech.assignment.kalaha.web.dto;

import io.swagger.annotations.ApiModelProperty;

public class PlayerDto {
    @ApiModelProperty(notes = "Player id", example = "42", required = true)
    private Long id;
    @ApiModelProperty(notes = "Login identifies Player", example = "nogibator", required = true)
    private String login;
    @ApiModelProperty(notes = "Displayed Player's name", example = "Alice", required = true)
    private String nickname;
    @ApiModelProperty(notes = "Number of Games won", example = "7", required = true)
    private Long wins;
    @ApiModelProperty(notes = "Number of Games lost", example = "6", required = true)
    private Long loses;

    public PlayerDto(
            Long id,
            String login,
            String nickname,
            Long wins,
            Long loses
    ) {
        this.id = id;
        this.login = login;
        this.nickname = nickname;
        this.wins = wins;
        this.loses = loses;
    }
}
