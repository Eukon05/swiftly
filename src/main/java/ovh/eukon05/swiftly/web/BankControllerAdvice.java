package ovh.eukon05.swiftly.web;

import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ovh.eukon05.swiftly.exception.BankAlreadyExistsException;
import ovh.eukon05.swiftly.exception.BankNotFoundException;
import ovh.eukon05.swiftly.web.dto.ResultDTO;

import java.util.Arrays;
import java.util.Objects;

import static ovh.eukon05.swiftly.util.Message.BANK_ALREADY_EXISTS;
import static ovh.eukon05.swiftly.util.Message.BANK_NOT_FOUND;

@ControllerAdvice
public class BankControllerAdvice {
    private static final ResultDTO BANK_NOT_FOUND_DTO = new ResultDTO(BANK_NOT_FOUND);
    private static final ResultDTO BANK_ALREADY_EXISTS_DTO = new ResultDTO(BANK_ALREADY_EXISTS);

    // Handlers for common exceptions, such as bank not found, already exists, etc.

    @ExceptionHandler(BankNotFoundException.class)
    public ResponseEntity<ResultDTO> handleBankNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BANK_NOT_FOUND_DTO);
    }

    @ExceptionHandler(BankAlreadyExistsException.class)
    public ResponseEntity<ResultDTO> handleBankAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(BANK_ALREADY_EXISTS_DTO);
    }

    // Handles DTO validation in their annotations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String msg = Arrays.stream(Objects.requireNonNull(ex.getDetailMessageArguments())).map(Object::toString).reduce("", String::concat);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResultDTO(msg));
    }

    // Handles DTO validation in their constructors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResultDTO(ex.getMostSpecificCause().getMessage()));
    }

    // Handles general constraint violations, but is used mostly to check the path variable of "/swift-codes/country/{code}"
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResultDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResultDTO(ex.getMessage()));
    }
}
