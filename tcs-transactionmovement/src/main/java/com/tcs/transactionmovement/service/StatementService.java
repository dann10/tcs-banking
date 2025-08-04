package com.tcs.transactionmovement.service;

import com.tcs.transactionmovement.dto.AccountStatement;
import com.tcs.transactionmovement.dto.MovementResponse;
import com.tcs.transactionmovement.model.Account;
import com.tcs.transactionmovement.repository.AccountRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatementService {

  private final AccountRepository accountRepository;
  private final MovementService movementService;

  public List<AccountStatement> generateClientAccountsStatement(
      Long clientId, LocalDate startDate, LocalDate endDate) {

    List<Account> accounts = accountRepository.findByClientIdAndStatusTrue(clientId);

    return accounts.stream()
        .map(
            account -> {
              LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
              LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

              List<MovementResponse> movements =
                  movementService.getMovementsByAccount(account.getAccountId(), start, end);

              return AccountStatement.builder()
                  .accountId(account.getAccountId())
                  .accountNumber(account.getAccountNumber())
                  .currentBalance(account.getCurrentBalance())
                  .accountType(account.getAccountType())
                  .movements(movements)
                  .build();
            })
        .toList();
  }
}
