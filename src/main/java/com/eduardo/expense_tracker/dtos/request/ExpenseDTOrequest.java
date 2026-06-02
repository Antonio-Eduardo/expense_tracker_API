package com.eduardo.expense_tracker.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTOrequest {
    private BigDecimal amount;
    private String description;
    private Instant expenseMoment;
    private Long monthlyExpenseId;
    private Long categoryId;

}
