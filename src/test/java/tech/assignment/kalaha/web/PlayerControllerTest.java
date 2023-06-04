package tech.assignment.kalaha.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.assignment.kalaha.exception.WrongPlayerException;
import tech.assignment.kalaha.model.Player;
import tech.assignment.kalaha.service.PlayerService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Test
    void canGetPlayer() throws Exception {
        Player player = new Player();
        player.setId(1L);
        player.setLogin("testLogin1");
        player.setNickname("Ada");
        player.setWins(5L);
        player.setLoses(2L);

        when(playerService.getPlayer(anyLong())).thenReturn(player);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/player/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("testLogin1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("Ada"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wins").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.loses").value(2));
    }

    @Test
    void errorGetPlayerIfDoesNotExist() throws Exception {
        when(playerService.getPlayer(anyLong())).thenThrow(new WrongPlayerException("nope"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/player/13"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists());
    }

    @Test
    void canCreatePlayer() throws Exception {
        Player player = new Player();
        player.setId(1L);
        player.setLogin("testLogin2");
        player.setNickname("Pascal");
        player.setWins(0L);
        player.setLoses(0L);

        when(playerService.createPlayer("testLogin2", "Pascal")).thenReturn(player);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/player")
                .param("login", "testLogin2")
                .param("nickname", "Pascal"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("testLogin2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("Pascal"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wins").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.loses").value(0));
    }
}
