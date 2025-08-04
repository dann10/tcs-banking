package com.tcs.transactionmovement.service;

import static com.tcs.transactionmovement.util.ErrorType.CONFLICT;
import static com.tcs.transactionmovement.util.ErrorType.NOT_FOUND;
import static com.tcs.transactionmovement.util.TransactionType.DEPOSIT;
import static com.tcs.transactionmovement.util.TransactionType.WITHDRAWAL;

import com.tcs.transactionmovement.dto.MovementRequest;
import com.tcs.transactionmovement.dto.MovementResponse;
import com.tcs.transactionmovement.exception.AccountNotFoundException;
import com.tcs.transactionmovement.exception.BankingException;
import com.tcs.transactionmovement.exception.InsufficientBalanceException;
import com.tcs.transactionmovement.mapper.MovementMapper;
import com.tcs.transactionmovement.model.Account;
import com.tcs.transactionmovement.model.MovementTransaction;
import com.tcs.transactionmovement.repository.AccountRepository;
import com.tcs.transactionmovement.repository.MovementRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovementService {

  private final MovementRepository movementRepository;
  private final AccountRepository accountRepository;
  private final MovementMapper movementMapper;

  @Transactional
  public MovementResponse createMovement(MovementRequest request) {
    Account account =
        accountRepository
            .findById(request.getAccountId())
            .orElseThrow(() -> new AccountNotFoundException(request.getAccountId()));
    validateTransactionType(request.getTransactionType());

    if (isWithdrawal(request)) {
      validateWithdrawal(account, request.getAmount());
    }

    BigDecimal newBalance = calculateNewBalance(account, request);

    MovementTransaction movement = movementMapper.toEntity(request);
    movement.setAccount(account);
    movement.setBalance(newBalance);

    account.setCurrentBalance(newBalance);
    accountRepository.save(account);

    MovementTransaction savedMovement = movementRepository.save(movement);
    return movementMapper.toResponse(savedMovement);
  }

  public List<MovementResponse> getMovementsByAccount(
      Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
    Account account =
        accountRepository
            .findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

    List<MovementTransaction> movements;

    if (startDate != null && endDate != null) {
      movements =
          movementRepository.findByAccountIdAndTransactionDateBetween(
              accountId, startDate, endDate);
    } else {
      movements = movementRepository.findByAccountId(accountId);
    }

    if (movements.isEmpty()) {
      throw new BankingException(
          "No se encontraron movimientos en la cuenta " + account.getAccountNumber(), NOT_FOUND);
    }

    return movements.stream().map(movementMapper::toResponse).toList();
  }

  private void validateTransactionType(String transactionType) {
    if (!transactionType.equalsIgnoreCase(WITHDRAWAL.getValue())
        && !transactionType.equalsIgnoreCase(DEPOSIT.getValue())) {
      throw new BankingException("Tipo de transaccion no valida", CONFLICT);
    }
  }

  private boolean isWithdrawal(MovementRequest request) {
    return WITHDRAWAL.getValue().equalsIgnoreCase(request.getTransactionType());
  }

  private void validateWithdrawal(Account account, BigDecimal amount) {
    if (account.getCurrentBalance().compareTo(amount) < 0) {
      throw new InsufficientBalanceException("Saldo insuficiente");
    }
  }

  private BigDecimal calculateNewBalance(Account account, MovementRequest request) {
    return DEPOSIT.getValue().equalsIgnoreCase(request.getTransactionType())
        ? account.getCurrentBalance().add(request.getAmount())
        : account.getCurrentBalance().subtract(request.getAmount());
  }
}
