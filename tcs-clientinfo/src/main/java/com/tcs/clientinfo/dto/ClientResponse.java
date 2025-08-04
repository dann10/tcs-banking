package com.tcs.clientinfo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientResponse {
  private Long id;
  private String clientCode;
  private String name;
  private String gender;
  private Integer age;
  private String identification;
  private String address;
  private String phone;
  private Boolean status;
}
