package com.tcs.transactionmovement.mapper;

import com.tcs.transactionmovement.dto.AccountRequest;
import com.tcs.transactionmovement.dto.AccountResponse;
import com.tcs.transactionmovement.model.Account;
import java.time.LocalDateTime;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    imports = {LocalDateTime.class, UUID.class})
public interface AccountMapper {
  @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
  @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
  @Mapping(target = "currentBalance", source = "currentBalance")
  @Mapping(target = "accountId", ignore = true)
  @Mapping(target = "accountNumber", expression = "java(generateAccountNumber())")
  @Mapping(target = "clientId", source = "request.clientId")
  @Mapping(target = "status", constant = "true")
  Account toEntity(AccountRequest request);

  @Mapping(target = "id", source = "accountId")
  @Mapping(target = "clientId", source = "clientId")
  @Mapping(target = "accountType", source = "accountType")
  @Mapping(target = "currentBalance", source = "currentBalance")
  @Mapping(target = "active", source = "status")
  AccountResponse toResponse(Account entity);

  default String generateAccountNumber() {
    return String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits())).substring(0, 13);
  }
}
