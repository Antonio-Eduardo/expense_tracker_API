package com.eduardo.expense_tracker.dtos.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyExpenseDTOresponse {
    private Long id;
    private BigDecimal monthTotal;
    private String month;
    private BigDecimal limitExpense;
    private Long bankAccountId;
}
