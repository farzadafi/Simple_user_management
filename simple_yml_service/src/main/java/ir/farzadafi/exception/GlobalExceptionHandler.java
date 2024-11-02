package ir.farzadafi.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DirectoryException.class)
    public ResponseEntity<ErrorDetails> userNameDuplicateExceptionHandler(DirectoryException e) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
}
