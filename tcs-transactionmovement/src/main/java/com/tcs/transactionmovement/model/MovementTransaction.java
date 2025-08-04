package com.tcs.transactionmovement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MOVEMENT_TRANSACTION", schema = "banco_db")
public class MovementTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "TRANSACTION_ID")
  private Long transactionId;

  @Column(name = "TRANSACTION_DATE", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime transactionDate = LocalDateTime.now();

  @Column(name = "TRANSACTION_TYPE", nullable = false, length = 20)
  private String transactionType;

  @Column(name = "AMOUNT", nullable = false, precision = 15, scale = 2)
  private BigDecimal amount;

  @Column(name = "BALANCE", nullable = false, precision = 15, scale = 2)
  private BigDecimal balance;

  @ManyToOne
  @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID", nullable = false)
  private Account account;

  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  @Column(name = "CREATED_AT", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}
