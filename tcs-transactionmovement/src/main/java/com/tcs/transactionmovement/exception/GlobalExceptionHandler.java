package com.tcs.transactionmovement.exception;

import static com.tcs.transactionmovement.util.ErrorType.CONFLICT;
import static com.tcs.transactionmovement.util.ErrorType.NOT_FOUND;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BankingException.class)
  public ResponseEntity<ExceptionDetails> handleBankingException(
      BankingException ex, WebRequest request) {
    ExceptionDetails errorDetails =
        new ExceptionDetails(
            LocalDateTime.now(),
            ex.getMessage(),
            request.getDescription(false),
            "BANKING_RULE_VIOLATION");
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<ExceptionDetails> handleAccountNotFoundException(
      AccountNotFoundException ex) {
    ExceptionDetails errorDetails =
        new ExceptionDetails(
            LocalDateTime.now(), ex.getMessage(), NOT_FOUND.getValue(), "Cuenta no encontrada");
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InsufficientBalanceException.class)
  public ResponseEntity<ExceptionDetails> handleInsufficientBalance(
      InsufficientBalanceException ex) {
    ExceptionDetails errorDetails =
        new ExceptionDetails(
            LocalDateTime.now(), ex.getMessage(), CONFLICT.getValue(), "Cuenta sin fondos");
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }
}
