package com.tcs.clientinfo.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientRequest {

  @NotBlank(message = "El nombre es obligatorio")
  @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
  @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre solo puede contener letras y espacios")
  private String name;

  @NotBlank(message = "El género es obligatorio")
  @Pattern(
      regexp = "^(MASCULINO|FEMENINO|OTRO)$",
      message = "El género debe ser MASCULINO, FEMENINO u OTRO")
  private String gender;

  @NotNull(message = "La edad es obligatoria")
  @Min(value = 18, message = "El cliente debe tener al menos 18 años")
  @Max(value = 120, message = "La edad máxima permitida es 120 años")
  private Integer age;

  @NotBlank(message = "La identificación es obligatoria")
  @Size(min = 8, max = 20, message = "La identificación debe tener entre 8 y 20 caracteres")
  @Pattern(
      regexp = "^[a-zA-Z0-9]*$",
      message = "La identificación solo puede contener letras y números")
  private String identification;

  @NotBlank(message = "La dirección es obligatoria")
  @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres")
  private String address;

  @NotBlank(message = "El teléfono es obligatorio")
  @Pattern(
      regexp = "^[+]?[(]?\\d{1,4}\\)?[-\\s.\\d]{8,15}$",
      message = "El teléfono debe ser un número válido")
  private String phone;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$",
      message =
          "La contraseña debe contener al menos 1 mayúscula, 1 minúscula, 1 número y 1 carácter especial, entre 8 y 10 caractéres")
  private String password;
}
