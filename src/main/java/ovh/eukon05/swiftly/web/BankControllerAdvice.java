package ovh.eukon05.swiftly.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ovh.eukon05.swiftly.exception.BankAlreadyExistsException;
import ovh.eukon05.swiftly.exception.BankNotFoundException;
import ovh.eukon05.swiftly.web.dto.ResultDTO;

import static ovh.eukon05.swiftly.util.Message.BANK_ALREADY_EXISTS;
import static ovh.eukon05.swiftly.util.Message.BANK_NOT_FOUND;

@ControllerAdvice
public class BankControllerAdvice {
    private static final ResultDTO BANK_NOT_FOUND_DTO = new ResultDTO(BANK_NOT_FOUND);
    private static final ResultDTO BANK_ALREADY_EXISTS_DTO = new ResultDTO(BANK_ALREADY_EXISTS);

    @ExceptionHandler(BankNotFoundException.class)
    public ResponseEntity<ResultDTO> handleBankNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BANK_NOT_FOUND_DTO);
    }

    @ExceptionHandler(BankAlreadyExistsException.class)
    public ResponseEntity<ResultDTO> handleBankAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(BANK_ALREADY_EXISTS_DTO);
    }
}
