package com.eduardo.expense_tracker.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ExpenseDTO {
    private BigDecimal amount;
    private String description;
    private Instant expenseMoment;
    private Long monthlyExpenseId;
    private Long categoryId;

}
