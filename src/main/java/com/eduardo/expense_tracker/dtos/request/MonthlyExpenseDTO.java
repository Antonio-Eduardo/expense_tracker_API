package com.eduardo.expense_tracker.dtos.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyExpenseDTO {

    private BigDecimal monthTotal;
    private String month;
    private BigDecimal limitExpense;
    private Long bankAccountId;
}
