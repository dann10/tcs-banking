package com.tcs.clientinfo.exception;

import com.tcs.clientinfo.util.ErrorType;
import lombok.Getter;

@Getter
public class BankingException extends RuntimeException {
  private final ErrorType errorType;

  public BankingException(String message, ErrorType errorType) {
    super(message);
    this.errorType = errorType;
  }
}
