package com.tcs.transactionmovement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementResponse {
  private Long transactionId;
  private LocalDateTime transactionDate;
  private String transactionType;
  private BigDecimal amount;
  private BigDecimal balance;
  private String description;
  private Long accountId;
}
