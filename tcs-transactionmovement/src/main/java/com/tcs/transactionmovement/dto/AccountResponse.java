package com.tcs.transactionmovement.dto;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
  private Long id;
  private String accountNumber;
  private Long clientId;
  private String accountType;
  private BigDecimal currentBalance;
  private boolean active;
}
