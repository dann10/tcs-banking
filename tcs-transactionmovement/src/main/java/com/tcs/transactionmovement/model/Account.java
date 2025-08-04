package com.tcs.transactionmovement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACCOUNT", schema = "BANKING_TCS")
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ACCOUNT_ID")
  private Long accountId;

  @Column(name = "ACCOUNT_NUMBER", unique = true, nullable = false, length = 20)
  private String accountNumber;

  @Column(name = "ACCOUNT_TYPE", nullable = false, length = 30)
  private String accountType;

  @Column(name = "CURRENT_BALANCE", nullable = false, precision = 15, scale = 2)
  private BigDecimal currentBalance;

  @Column(name = "STATUS", columnDefinition = "BOOLEAN DEFAULT TRUE")
  private Boolean status = true;

  @Column(name = "CLIENT_ID", nullable = false)
  private Long clientId;

  @Column(name = "CREATED_AT", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "UPDATED_AT")
  private LocalDateTime updatedAt = LocalDateTime.now();
}
