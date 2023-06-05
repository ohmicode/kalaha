package tech.assignment.kalaha.repository;

import org.springframework.data.repository.CrudRepository;
import tech.assignment.kalaha.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, Long> {
    Optional<Player> findById(long id);
    Optional<Player> findByLogin(String login);
    List<Player> findTop10ByOrderByWinsDescLosesAsc();
}
