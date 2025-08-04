package com.tcs.clientinfo.config;

import com.tcs.clientinfo.router.AccountRoute;
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

  @Bean(name = "accountRoute")
  public AccountRoute accountRoute() {
    return new AccountRoute();
  }
}
