package com.tcs.transactionmovement.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatement {
    private Long accountId;
    private String accountNumber;
    private String accountType;
    private BigDecimal currentBalance;
    private List<MovementResponse> movements;
}