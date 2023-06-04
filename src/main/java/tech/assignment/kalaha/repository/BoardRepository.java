package tech.assignment.kalaha.repository;

import org.springframework.data.repository.CrudRepository;
import tech.assignment.kalaha.model.Board;

import java.util.Optional;

public interface BoardRepository extends CrudRepository<Board, Long> {
    Optional<Board> findById(long id);
}
