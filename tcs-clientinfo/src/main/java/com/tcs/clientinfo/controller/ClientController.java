package com.tcs.clientinfo.controller;

import com.tcs.clientinfo.dto.ClientRequest;
import com.tcs.clientinfo.dto.ClientResponse;
import com.tcs.clientinfo.service.ClientService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClientController {

  private final ClientService clientService;

  @PostMapping
  public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(request));
  }

  @GetMapping
  public ResponseEntity<ClientResponse> getClient(@RequestParam("id") Long id) {
    return ResponseEntity.ok(clientService.getClient(id));
  }

  @PutMapping
  public ResponseEntity<ClientResponse> updateClient(
      @RequestParam("id") Long id, @Valid @RequestBody ClientRequest request) {
    return ResponseEntity.ok(clientService.updateClient(id, request));
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteClient(@RequestParam("id") Long id) {
    clientService.deleteClient(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/all")
  public ResponseEntity<List<ClientResponse>> getClientList() {
    return ResponseEntity.ok(clientService.getClientList());
  }

  @GetMapping("/active")
  public ResponseEntity<Boolean> isClientActive(@RequestParam("id") Long id) {
    return ResponseEntity.ok(clientService.isClientActive(id));
  }
}
