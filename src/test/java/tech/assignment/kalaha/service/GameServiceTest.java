package tech.assignment.kalaha.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.assignment.kalaha.exception.EmptyFieldException;
import tech.assignment.kalaha.exception.GameNotFoundException;
import tech.assignment.kalaha.exception.WrongPlayerException;
import tech.assignment.kalaha.model.Board;
import tech.assignment.kalaha.model.Player;
import tech.assignment.kalaha.repository.BoardRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static tech.assignment.kalaha.service.TestDataProvider.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private PlayerService playerService;
    @Mock
    private BoardRepository boardRepository;

    @Test
    void canCreateGame() {
        Player player = createFirstPlayer();

        when(playerService.getPlayer(player.getId())).thenReturn(player);
        when(boardRepository.save(any())).thenAnswer(returnsFirstArg());

        GameService gameService = new GameService(playerService, boardRepository, new KalahaEngine());
        Board board = gameService.createGame(player.getId());

        assertEquals(player.getId(), board.getPlayer1Id());
        assertEquals(0, board.getPool1());
        assertEquals(0, board.getPool2());

        verify(playerService, times(1)).getPlayer(player.getId());
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    void canNotCreateGameForUnknownUser() {
        Long playerId = 666L;

        when(playerService.getPlayer(playerId)).thenThrow(new WrongPlayerException("no way!"));

        GameService gameService = new GameService(playerService, boardRepository, new KalahaEngine());
        assertThrows(WrongPlayerException.class, () -> {
            Board board = gameService.createGame(playerId);
        });

        verify(playerService, times(1)).getPlayer(playerId);
        verify(boardRepository, never()).save(any());
    }

    @Test
    void returnsExistingBoard() {
        Board board = createFirstPlayerWonBoard();

        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        GameService gameService = new GameService(playerService, boardRepository, new KalahaEngine());
        Board response = gameService.getBoard(board.getId());

        assertEquals(board.getId(), response.getId());
        verify(boardRepository, times(1)).findById(board.getId());
    }

    @Test
    void exceptionForNonexistingBoard() {
        Long boardId = 999L;

        when(boardRepository.findById(boardId)).thenThrow(new GameNotFoundException(boardId));

        GameService gameService = new GameService(playerService, boardRepository, new KalahaEngine());
        assertThrows(GameNotFoundException.class, () -> {
            Board response = gameService.getBoard(boardId);
        });

        verify(boardRepository, times(1)).findById(any());
        verify(boardRepository, never()).save(any());
    }

    @Test
    void canMakeLegalMove() {
        Player player = createFirstPlayer();
        Board board = createCustomBoard(
                Arrays.asList(2, 2, 2, 2),
                Arrays.asList(2, 2, 2, 2),
                3, 5
        );
        board.setId(42L);
        int move = 3;

        when(playerService.getPlayer(player.getId())).thenReturn(player);
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(boardRepository.save(any())).thenAnswer(returnsFirstArg());

        GameService gameService = new GameService(playerService, boardRepository, new KalahaEngine());
        Board response = gameService.makeMove(board.getId(), player.getId(), move);

        assertEquals(board.getId(), response.getId());
        assertEquals(4, response.getPool1());
        assertEquals(3, response.getSide2().get(0));
        verify(boardRepository, times(1)).findById(board.getId());
        verify(boardRepository, times(1)).save(any());
    }

    @Test
    void canNotMakeIllegalMove() {
        Player player = createFirstPlayer();
        Board board = createCustomBoard(
                Arrays.asList(0, 3, 3),
                Arrays.asList(1, 1, 1),
                7, 1
        );
        board.setId(58L);
        int move = 0;

        when(playerService.getPlayer(player.getId())).thenReturn(player);
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        GameService gameService = new GameService(playerService, boardRepository, new KalahaEngine());
        assertThrows(EmptyFieldException.class, () -> {
            Board response = gameService.makeMove(board.getId(), player.getId(), move);
        });

        verify(boardRepository, times(1)).findById(board.getId());
        verify(boardRepository, never()).save(any());
    }
}
