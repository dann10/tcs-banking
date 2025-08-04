package com.tcs.transactionmovement.repository;

import com.tcs.transactionmovement.model.MovementTransaction;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<MovementTransaction, Long> {

  @Query(
      "SELECT m FROM MovementTransaction m WHERE m.account.accountId = :accountId ORDER BY m.transactionDate DESC")
  List<MovementTransaction> findByAccountId(@Param("accountId") Long accountId);

  @Query(
      "SELECT m FROM MovementTransaction m WHERE m.account.accountId = :accountId "
          + "AND m.transactionDate BETWEEN :startDate AND :endDate "
          + "ORDER BY m.transactionDate DESC")
  List<MovementTransaction> findByAccountIdAndTransactionDateBetween(
      @Param("accountId") Long accountId,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);
}
