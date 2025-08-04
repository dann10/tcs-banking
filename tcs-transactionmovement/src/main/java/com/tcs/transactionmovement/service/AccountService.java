package com.tcs.transactionmovement.service;

import static com.tcs.transactionmovement.util.ErrorType.*;

import com.tcs.transactionmovement.client.ClientFeignClient;
import com.tcs.transactionmovement.dto.AccountRequest;
import com.tcs.transactionmovement.dto.AccountResponse;
import com.tcs.transactionmovement.dto.ClientResponse;
import com.tcs.transactionmovement.exception.AccountNotFoundException;
import com.tcs.transactionmovement.exception.BankingException;
import com.tcs.transactionmovement.mapper.AccountMapper;
import com.tcs.transactionmovement.model.Account;
import com.tcs.transactionmovement.repository.AccountRepository;
import feign.FeignException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountService {

  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;
  private final ClientFeignClient clientFeignClient;

  @Transactional
  public AccountResponse createAccount(AccountRequest request) {
    ClientResponse client = validateClient(request.getClientId());

    Account account = accountMapper.toEntity(request);
    account.setClientId(client.getId());

    if (request.getCurrentBalance() != null) {
      account.setCurrentBalance(request.getCurrentBalance());
    } else {
      account.setCurrentBalance(BigDecimal.ZERO);
    }

    Account savedAccount = accountRepository.save(account);
    log.info("Cuenta creada exitosamente: {}", savedAccount.getAccountNumber());
    return accountMapper.toResponse(savedAccount);
  }

  @Transactional(readOnly = true)
  public AccountResponse getAccount(Long id) {
    Account account =
        accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));

    verifyClientStatus(account.getClientId());

    return accountMapper.toResponse(account);
  }

  @Transactional
  public void deleteAccount(Long id) {
    Account account =
        accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));

    verifyClientStatus(account.getClientId());

    account.setStatus(false);
    accountRepository.save(account);
    log.info("Cuenta {} desactivada con exito", id);
  }

  @Transactional(readOnly = true)
  public List<AccountResponse> getAccountsByClient(Long clientId) {
    verifyClientStatus(clientId);

    return accountRepository.findByClientIdAndStatusTrue(clientId).stream()
        .map(accountMapper::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public BigDecimal getAccountBalance(String accountNumber) {
    Account account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(
                () -> new BankingException("No existe cuenta con ese numero asociado", NOT_FOUND));

    verifyClientStatus(account.getClientId());

    return account.getCurrentBalance();
  }

  @Transactional
  public void disableClientAccounts(Long clientId) {
    List<Account> activeAccounts = accountRepository.findByClientIdAndStatusTrue(clientId);
    if (activeAccounts.isEmpty()) {
      log.error("No se encontraron cuentas activas para el cliente con ID: {}", clientId);
      throw new BankingException("El cliente no tiene cuentas activas para desactivar", NOT_FOUND);
    }

    activeAccounts.forEach(
        account -> {
          account.setStatus(false);
          account.setUpdatedAt(LocalDateTime.now());
          log.info("Desactivando cuenta {} del cliente {}", account.getAccountNumber(), clientId);
        });

    List<Account> updatedAccounts = accountRepository.saveAll(activeAccounts);
    log.info("Se desactivaron {} cuentas del cliente {}", updatedAccounts.size(), clientId);
  }

  private ClientResponse validateClient(Long clientId) {
    try {
      ResponseEntity<ClientResponse> response = clientFeignClient.getClient(clientId);

      if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
        throw new BankingException("Servicio de clientes no disponible", SERVICE_UNAVAILABLE);
      }

      ClientResponse client = response.getBody();
      if (Boolean.FALSE.equals(client.getStatus())) {
        throw new BankingException("Cliente inhabilitado", CONFLICT);
      }

      return client;
    } catch (FeignException.NotFound e) {
      throw new BankingException("Cliente no encontrado", NOT_FOUND);
    }
  }

  private void verifyClientStatus(Long clientId) {
    try {
      ResponseEntity<Boolean> response = clientFeignClient.isClientActive(clientId);
      if (!response.getStatusCode().is2xxSuccessful()
          || response.getBody() == null
          || Boolean.TRUE.equals(!response.getBody())) {
        throw new BankingException("Cliente inhabilitado", CONFLICT);
      }
    } catch (FeignException.NotFound e) {
      throw new BankingException("Cliente no encontrado", NOT_FOUND);
    }
  }
}
