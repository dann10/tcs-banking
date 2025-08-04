package com.tcs.transactionmovement.config;

import com.tcs.transactionmovement.router.AccountRoute;
import com.tcs.transactionmovement.service.AccountService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Slf4j
@RequiredArgsConstructor
public class RouteConfig {
  private final AccountService accountService;

  @Bean(name = "accountRoute")
  public AccountRoute accountRoute() {
    return new AccountRoute(accountService);
  }
}
