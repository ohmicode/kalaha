package tech.assignment.kalaha.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.assignment.kalaha.exception.EmptyFieldException;
import tech.assignment.kalaha.exception.GameNotFoundException;
import tech.assignment.kalaha.exception.GameStateException;
import tech.assignment.kalaha.exception.WrongPlayerException;
import tech.assignment.kalaha.web.dto.ErrorDto;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class.getName());

    private static final String GLOBAL = "global";
    private static final String GAME = "game";

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDto> handleThrowable(Throwable exception) {
        UUID traceId = UUID.randomUUID();
        ErrorDto error = new ErrorDto(traceId, GLOBAL, "Internal error");
        logger.error(traceId + ". Something went wrong: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorDto> handleGameNotFoundException(GameNotFoundException exception) {
        UUID traceId = UUID.randomUUID();
        ErrorDto error = new ErrorDto(traceId, GLOBAL, exception.getMessage());
        logger.warn(traceId + ". GameNotFoundException: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<ErrorDto> handleEmptyFieldException(EmptyFieldException exception) {
        UUID traceId = UUID.randomUUID();
        ErrorDto error = new ErrorDto(traceId, GAME, exception.getMessage());
        logger.warn(traceId + ". EmptyFieldException: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(GameStateException.class)
    public ResponseEntity<ErrorDto> handleGameStateException(GameStateException exception) {
        UUID traceId = UUID.randomUUID();
        ErrorDto error = new ErrorDto(traceId, GAME, exception.getMessage());
        logger.warn(traceId + ". GameStateException: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(WrongPlayerException.class)
    public ResponseEntity<ErrorDto> handleWrongPlayerException(WrongPlayerException exception) {
        UUID traceId = UUID.randomUUID();
        ErrorDto error = new ErrorDto(traceId, GAME, exception.getMessage());
        logger.warn(traceId + ". WrongPlayerException: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
