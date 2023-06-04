package tech.assignment.kalaha.repository;

import org.springframework.data.repository.CrudRepository;
import tech.assignment.kalaha.model.Player;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, Long> {
    Optional<Player> findById(long id);
}
