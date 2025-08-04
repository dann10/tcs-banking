package com.tcs.transactionmovement.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorType {
  DUPLICATE_ENTRY("ERR-001"),
  INVALID_DATA("ERR-002"),
  NOT_FOUND("ERR-003"),
  CONFLICT("ERR-004"),
  SERVICE_UNAVAILABLE("ERR-005");

  private final String value;
}
