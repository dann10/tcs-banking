package com.tcs.transactionmovement.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.transactionmovement.dto.DisableAccount;
import com.tcs.transactionmovement.service.AccountService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
@RequiredArgsConstructor
public class AccountProcessor implements Processor {
  private final AccountService accountService;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void process(Exchange exchange) {
    String body = exchange.getIn().getBody(String.class);

    try {
      DisableAccount disableAccounts = objectMapper.readValue(body, DisableAccount.class);
      accountService.disableClientAccounts(disableAccounts.clientId());
    } catch (IOException e) {
      throw new IllegalStateException("Error al parsear Json: " + e.getMessage());
    }
  }
}
