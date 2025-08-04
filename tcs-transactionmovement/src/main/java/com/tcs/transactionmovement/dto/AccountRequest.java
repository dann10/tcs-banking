package com.tcs.transactionmovement.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {
  @NotNull(message = "El id del cliente es requerido")
  private Long clientId;

  @NotBlank(message = "El tipo de cuenta es requerido")
  @Pattern(
      regexp = "^(AHORROS|CORRIENTE)$",
      message = "Tipo de cuenta no válido. Valores permitidos: AHORROS, CORRIENTE")
  private String accountType;

  @DecimalMin(value = "0.0", message = "El balance inicial no puede ser negativo")
  private BigDecimal currentBalance;
}
