package com.eduardo.expense_tracker.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandartError {
    Instant timestamp;
    Integer status;
    String error;
    String message;
}
