package com.tcs.transactionmovement.client;

import com.tcs.transactionmovement.dto.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "client", url = "${client.url}")
public interface ClientFeignClient {
  @GetMapping
  ResponseEntity<ClientResponse> getClient(@RequestParam("id") Long id);

    @GetMapping("/active")
    ResponseEntity<Boolean> isClientActive(@RequestParam("id") Long id);
}
