package com.tcs.transactionmovement.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AccountType {
  SAVINGS_ACCOUNT("AHORROS"),
  CURRENT_ACCOUNT("CORRIENTE");

  private final String value;
}
