package com.tcs.transactionmovement.controller;

import com.tcs.transactionmovement.dto.AccountRequest;
import com.tcs.transactionmovement.dto.AccountResponse;
import com.tcs.transactionmovement.service.AccountService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping
  public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
  }

  @GetMapping
  public ResponseEntity<AccountResponse> getAccount(@RequestParam("id") Long id) {
    return ResponseEntity.ok(accountService.getAccount(id));
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteAccount(@RequestParam("id") Long id) {
    accountService.deleteAccount(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/cliente")
  public ResponseEntity<List<AccountResponse>> getAccountsByClient(
      @RequestParam("clientId") Long clientId) {
    return ResponseEntity.ok(accountService.getAccountsByClient(clientId));
  }

  @GetMapping("/saldo")
  public ResponseEntity<BigDecimal> getAccountBalance(
      @RequestParam("accountNumber") String accountNumber) {
    return ResponseEntity.ok(accountService.getAccountBalance(accountNumber));
  }
}
