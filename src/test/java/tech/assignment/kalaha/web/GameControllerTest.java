package tech.assignment.kalaha.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.assignment.kalaha.exception.EmptyFieldException;
import tech.assignment.kalaha.exception.GameNotFoundException;
import tech.assignment.kalaha.model.Board;
import tech.assignment.kalaha.service.GameService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tech.assignment.kalaha.service.TestDataProvider.createCustomBoard;

@WebMvcTest(controllers = GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    void canGetGame() throws Exception {
        Board board = createCustomBoard(
                Arrays.asList(1, 1, 1),
                Arrays.asList(2, 2, 2),
                10, 20
        );
        board.setId(42L);

        when(gameService.getBoard(anyLong())).thenReturn(board);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/game/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(42))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits1[0]").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits1[1]").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits1[2]").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits2[0]").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits2[1]").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits2[2]").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pool1").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pool2").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameStatus").value("ONGOING"));
    }

    @Test
    void notFoundIfDoesNotExist() throws Exception {
        when(gameService.getBoard(anyLong())).thenThrow(new GameNotFoundException(13L));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/game/13"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("global"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists());
    }

    @Test
    void canCreateGame() throws Exception {
        Board board = createCustomBoard(
                Arrays.asList(5, 5),
                Arrays.asList(5, 5),
                0, 0
        );
        board.setId(6L);

        when(gameService.createGame(anyLong())).thenReturn(board);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/game")
                .param("playerId", "7"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits1[0]").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits1[1]").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits2[0]").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits2[1]").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pool1").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pool2").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameStatus").value("ONGOING"));
    }

    @Test
    void canMakeMove() throws Exception {
        Board board = createCustomBoard(
                Arrays.asList(3, 3),
                Arrays.asList(3, 3),
                1, 1
        );
        board.setId(6L);

        when(gameService.makeMove(6, 7, 1)).thenReturn(board);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/game/6/move")
                .param("playerId", "7")
                .param("move", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits1[0]").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits1[1]").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits2[0]").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pits2[1]").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pool1").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pool2").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameStatus").value("ONGOING"));
    }

    @Test
    void canNotDoIllegalMove() throws Exception {
        when(gameService.makeMove(10, 5, 2)).thenThrow(new EmptyFieldException("empty"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/game/10/move")
                .param("playerId", "5")
                .param("move", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("game"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists());
    }
}
