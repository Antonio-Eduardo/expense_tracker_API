package com.eduardo.expense_tracker.dtos.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CategoryDTO {
    private BigDecimal notifyLimit;
    private String name;
    private List<ExpenseDTO> expenseDTOS;
}
