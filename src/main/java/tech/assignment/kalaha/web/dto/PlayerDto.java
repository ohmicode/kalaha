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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getWins() {
        return wins;
    }

    public void setWins(Long wins) {
        this.wins = wins;
    }

    public Long getLoses() {
        return loses;
    }

    public void setLoses(Long loses) {
        this.loses = loses;
    }
}
