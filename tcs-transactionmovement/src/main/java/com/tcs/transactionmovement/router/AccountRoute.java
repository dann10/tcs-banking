package com.tcs.transactionmovement.router;

import com.tcs.transactionmovement.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;

@RequiredArgsConstructor
@Slf4j
public class AccountRoute extends RouteBuilder {
  private final AccountService accountService;

  @Override
  public void configure() {
    onException(Exception.class)
        .handled(true)
        .log(
            "ERROR: ${exchangeProperty.routeId}: ${exception.class.simpleName} - ${exception.message}")
        .logStackTrace(true);

    from("jms:queue:DISABLE_ACCOUNTS")
        .routeId("disableAccounts")
        .to("disruptor:process_disableAccounts?size=1000&blockWhenFull=true");
    from("disruptor:process_disableAccounts?concurrentConsumers=10")
        .log("Solicitud de desactivacion para el cliente: ${body}")
        .process(new AccountProcessor(accountService))
        .log("Desactivacion completada con exito");
  }
}
