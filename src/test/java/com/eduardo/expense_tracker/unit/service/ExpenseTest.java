package com.eduardo.expense_tracker.unit.service;

import com.eduardo.expense_tracker.dtos.request.CategoryDTOrequest;
import com.eduardo.expense_tracker.dtos.request.ExpenseDTOrequest;
import com.eduardo.expense_tracker.dtos.response.ExpenseDTOresponse;
import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.entities.Expense;
import com.eduardo.expense_tracker.repositories.ExpenseRepository;
import com.eduardo.expense_tracker.services.ExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseTest {

    @Mock
    private ExpenseRepository repository;

    @InjectMocks
    private ExpenseService service;

    @Test
    public void deveriaCriarUmaDespesa(){
        Expense expense = new Expense();
        expense.setDescription("Uber");

        ExpenseDTOrequest expenseDTOrequest = new ExpenseDTOrequest();
        expenseDTOrequest.setDescription("Uber");

        when(repository.save(any(Expense.class))).thenReturn(expense);

        ExpenseDTOresponse dtOresponse = service.insertExpense(expenseDTOrequest);

        assertNotNull(dtOresponse);
        assertEquals("Uber", dtOresponse.getDescription());

        verify(repository).save(any(Expense.class));
    }
}
