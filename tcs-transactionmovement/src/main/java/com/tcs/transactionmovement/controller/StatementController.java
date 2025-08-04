package com.tcs.transactionmovement.controller;

import com.tcs.transactionmovement.dto.AccountStatement;
import com.tcs.transactionmovement.service.StatementService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class StatementController {

  private final StatementService statementService;

  @GetMapping
  public ResponseEntity<List<AccountStatement>> getClientAccountsStatements(
      @RequestParam Long clientId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate) {
    return ResponseEntity.ok(
        statementService.generateClientAccountsStatement(clientId, startDate, endDate));
  }
}
