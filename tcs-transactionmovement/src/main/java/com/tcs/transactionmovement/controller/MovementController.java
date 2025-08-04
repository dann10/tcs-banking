package com.tcs.transactionmovement.controller;

import com.tcs.transactionmovement.dto.MovementRequest;
import com.tcs.transactionmovement.dto.MovementResponse;
import com.tcs.transactionmovement.service.MovementService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovementController {

  private final MovementService movementService;

  @PostMapping
  public ResponseEntity<MovementResponse> createMovement(
      @Valid @RequestBody MovementRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(movementService.createMovement(request));
  }

  @GetMapping
  public ResponseEntity<List<MovementResponse>> getMovementsByAccount(
      @RequestParam("accountId") Long accountId,
      @RequestParam(required = false) LocalDateTime startDate,
      @RequestParam(required = false) LocalDateTime endDate) {
    return ResponseEntity.ok(movementService.getMovementsByAccount(accountId, startDate, endDate));
  }
}
