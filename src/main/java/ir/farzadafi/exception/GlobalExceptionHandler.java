package ir.farzadafi.exception;

import ir.farzadafi.dto.ErrorDetails;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.NoRouteToHostException;
import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InformationDuplicateException.class)
    public ResponseEntity<ErrorDetails> userNameDuplicateExceptionHandler(InformationDuplicateException e) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                "TotalErrors:" + ex.getErrorCount() + " First Error:" + Objects.requireNonNull(ex.getFieldError()).getDefaultMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> constraintViolationExceptionHandler(ConstraintViolationException e) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetails> notFoundExceptionHandler(NotFoundException e) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(TokenExpireException.class)
    public ResponseEntity<ErrorDetails> tokenExpireExceptionHandler(TokenExpireException e) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoRouteToHostException.class)
    public ResponseEntity<ErrorDetails> noRouteToHostExceptionHandler(NoRouteToHostException e) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorDetails> numberFormatExceptionHandler() {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Please send a number for age");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> badCredentialsExceptionHandler(BadCredentialsException e) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> accessDeniedExceptionHandler(AccessDeniedException e) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}