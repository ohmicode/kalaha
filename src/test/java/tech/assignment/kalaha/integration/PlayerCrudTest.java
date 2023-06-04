package tech.assignment.kalaha.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.assignment.kalaha.model.Player;
import tech.assignment.kalaha.repository.PlayerRepository;
import tech.assignment.kalaha.service.PlayerService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PlayerCrudTest {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerService playerService;

    @Test
    void playerCreationTest() {
        String login = "login1";
        String nickname = "Alice";

        Player created = playerService.createPlayer(login, nickname);
        Player retrieved = playerService.getPlayer(created.getId());
        Player saved = playerRepository.findById(created.getId()).orElseThrow();

        assertEquals(login, created.getLogin());
        assertEquals(login, retrieved.getLogin());
        assertEquals(login, saved.getLogin());
        assertEquals(nickname, created.getNickname());
        assertEquals(nickname, retrieved.getNickname());
        assertEquals(nickname, saved.getNickname());
        assertEquals(0, created.getWins());
        assertEquals(0, retrieved.getWins());
        assertEquals(0, saved.getWins());
        assertEquals(0, created.getLoses());
        assertEquals(0, retrieved.getLoses());
        assertEquals(0, saved.getLoses());
    }

    @Test
    void existingPlayerRetrievalTest() {
        String login = "login2";
        String nickname = "Bob";

        Player player = new Player();
        player.setLogin(login);
        player.setNickname(nickname);
        Long id = playerRepository.save(player).getId();

        Player retrieved = playerService.getPlayer(id);

        assertEquals(login, retrieved.getLogin());
        assertEquals(nickname, retrieved.getNickname());
        assertEquals(0, retrieved.getWins());
        assertEquals(0, retrieved.getLoses());
    }
}
