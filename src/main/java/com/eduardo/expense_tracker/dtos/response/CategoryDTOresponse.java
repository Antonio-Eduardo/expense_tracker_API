package com.eduardo.expense_tracker.dtos.response;

import com.eduardo.expense_tracker.entities.Expense;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class CategoryDTOresponse {
    private Long id;
    private BigDecimal notifyLimit;
    private String name;
    private Set<ExpenseDTOresponse> expenseDTOS;
}
