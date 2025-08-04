package com.tcs.transactionmovement.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransactionType {
  DEPOSIT("DEPOSITO"),
  WITHDRAWAL("RETIRO");

  private final String value;
}
