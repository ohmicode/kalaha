package tech.assignment.kalaha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.assignment.kalaha.exception.WrongPlayerException;
import tech.assignment.kalaha.model.Player;
import tech.assignment.kalaha.repository.PlayerRepository;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player getPlayer(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new WrongPlayerException("Player " + playerId + " does not exist"));
    }

    public Player createPlayer(String login, String nickname) {
        Player player = new Player();
        player.setLogin(login);
        player.setNickname(nickname);
        playerRepository.save(player);
        return player;
    }
}
