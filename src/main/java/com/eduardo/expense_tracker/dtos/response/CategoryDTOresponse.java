package com.eduardo.expense_tracker.dtos.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CategoryDTOresponse {
    private BigDecimal notifyLimit;
    private String name;
    private List<ExpenseDTOresponse> expenseDTOS;
}
