package tech.assignment.kalaha.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.assignment.kalaha.exception.WrongPlayerException;
import tech.assignment.kalaha.model.Player;
import tech.assignment.kalaha.repository.PlayerRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static tech.assignment.kalaha.service.TestDataProvider.createFirstPlayer;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Test
    void canCreatePlayer() {
        String login = "my@email.com";
        String nickname = "Bot";

        when(playerRepository.save(any())).thenAnswer(returnsFirstArg());

        PlayerService playerService = new PlayerService(playerRepository);
        Player player = playerService.createPlayer(login, nickname);

        assertNotNull(player);
        assertEquals(login, player.getLogin());
        assertEquals(nickname, player.getNickname());
        verify(playerRepository, times(1)).save(any());
    }

    @Test
    void canLoadPlayer() {
        Player player = createFirstPlayer();
        player.setId(42L);

        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));

        PlayerService playerService = new PlayerService(playerRepository);
        Player result = playerService.getPlayer(player.getId());

        assertNotNull(result);
        assertEquals(player.getId(), result.getId());
        assertEquals(player.getLogin(), result.getLogin());
        assertEquals(player.getNickname(), result.getNickname());
        assertEquals(player.getWins(), result.getWins());
        assertEquals(player.getLoses(), result.getLoses());
        verify(playerRepository, times(1)).findById(player.getId());
    }

    @Test
    void canNotLoadNonexistingPlayer() {
        Long playerId = 13L;

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        PlayerService playerService = new PlayerService(playerRepository);
        assertThrows(WrongPlayerException.class, () -> {
            Player result = playerService.getPlayer(playerId);
        });

        verify(playerRepository, times(1)).findById(playerId);
    }
}
