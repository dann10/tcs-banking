package com.tcs.clientinfo.router;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

@RequiredArgsConstructor
@Slf4j
public class AccountRoute extends RouteBuilder {
  @Override
  public void configure() {
    onException(Exception.class)
        .handled(true)
        .log(
            "ERROR: ${exchangeProperty.routeId}: ${exception.class.simpleName} - ${exception.message}")
        .logStackTrace(true);

    from("direct:disable_accounts")
        .doTry()
        .to("jms:queue:DISABLE_ACCOUNTS")
        .log("Solicitud enviada para desactivación de las cuentas para el cliente: ${body} ")
        .doCatch(Exception.class)
        .log(LoggingLevel.ERROR, "Error al enviar mensaje a la cola: ${exception.message}")
        .endDoTry()
        .end();
  }
}
