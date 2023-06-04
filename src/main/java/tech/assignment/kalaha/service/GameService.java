package tech.assignment.kalaha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.assignment.kalaha.exception.GameNotFoundException;
import tech.assignment.kalaha.model.Board;
import tech.assignment.kalaha.model.Player;
import tech.assignment.kalaha.repository.BoardRepository;

@Service
public class GameService {

    private PlayerService playerService;
    private BoardRepository boardRepository;
    private KalahaEngine kalahaEngine;

    @Autowired
    public GameService(
            PlayerService playerService,
            BoardRepository boardRepository,
            KalahaEngine kalahaEngine
    ) {
        this.playerService = playerService;
        this.boardRepository = boardRepository;
        this.kalahaEngine = kalahaEngine;
    }

    public Board createGame(Long playerId) {
        Player player = playerService.getPlayer(playerId);
        Board board = kalahaEngine.createGame(player);
        boardRepository.save(board);
        return board;
    }

    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    public Board makeMove(long boardId, long playerId, int pitNumber) {
        Board board = getBoard(boardId);
        Player player = playerService.getPlayer(playerId);
        Board changed = kalahaEngine.makeMove(board, player, pitNumber);
        return boardRepository.save(changed);
    }
}
