package tech.assignment.kalaha.exception;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(long id) {
        super("Game #" + id + "not found");
    }
}
