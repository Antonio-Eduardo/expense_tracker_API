package com.eduardo.expense_tracker.dtos.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyExpenseDTOrequest {

    private BigDecimal monthTotal;
    private String month;
    private BigDecimal limitExpense;
    private Long bankAccountId;
}
