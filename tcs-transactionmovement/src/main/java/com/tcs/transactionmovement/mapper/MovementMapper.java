package com.tcs.transactionmovement.mapper;

import static com.tcs.transactionmovement.util.ErrorType.CONFLICT;
import static com.tcs.transactionmovement.util.TransactionType.DEPOSIT;
import static com.tcs.transactionmovement.util.TransactionType.WITHDRAWAL;

import com.tcs.transactionmovement.dto.MovementRequest;
import com.tcs.transactionmovement.dto.MovementResponse;
import com.tcs.transactionmovement.exception.BankingException;
import com.tcs.transactionmovement.model.MovementTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MovementMapper {

  @Mapping(target = "transactionId", ignore = true)
  @Mapping(target = "transactionDate", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "balance", ignore = true)
  @Mapping(target = "account", ignore = true)
  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(
      target = "transactionType",
      source = "request.transactionType",
      qualifiedByName = "mapTransactionType")
  MovementTransaction toEntity(MovementRequest request);

  @Mapping(target = "accountId", source = "account.accountId")
  MovementResponse toResponse(MovementTransaction entity);

  @Named("mapTransactionType")
  default String mapTransactionType(String transactionType) {
    if (!transactionType.equalsIgnoreCase(WITHDRAWAL.getValue())
        && !transactionType.equalsIgnoreCase(DEPOSIT.getValue())) {
      throw new BankingException("Tipo de transaccion no valida", CONFLICT);
    }
    return transactionType;
  }
}
