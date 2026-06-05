package com.eduardo.expense_tracker.unit.service;

import com.eduardo.expense_tracker.dtos.request.CategoryDTOrequest;
import com.eduardo.expense_tracker.dtos.request.ExpenseDTOrequest;
import com.eduardo.expense_tracker.dtos.response.ExpenseDTOresponse;
import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.entities.Expense;
import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.repositories.CategoryRepository;
import com.eduardo.expense_tracker.repositories.ExpenseRepository;
import com.eduardo.expense_tracker.repositories.MonthlyExpenseRepository;
import com.eduardo.expense_tracker.services.ExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseTest {

    @Mock
    private ExpenseRepository repository;

    @Mock
    private MonthlyExpenseRepository monthlyExpenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ExpenseService service;

    @Test
    public void deveriaCriarUmaDespesa(){
        Category category = new Category();
        category.setId(1L);

        MonthlyExpense monthlyExpense = new MonthlyExpense();
        monthlyExpense.setId(1L);

        Expense expense = new Expense();
        expense.setDescription("Uber");
        expense.setMonthlyExpense(monthlyExpense);
        expense.setCategory(category);

        ExpenseDTOrequest expenseDTOrequest = new ExpenseDTOrequest();
        expenseDTOrequest.setDescription("Uber");
        expenseDTOrequest.setMonthlyExpenseId(1L);
        expenseDTOrequest.setCategoryId(1L);

        when(repository.save(any(Expense.class))).thenReturn(expense);
        when(monthlyExpenseRepository.findById(any(Long.class))).thenReturn(Optional.of(monthlyExpense));
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));

        ExpenseDTOresponse dtOresponse = service.insertExpense(expenseDTOrequest);

        assertNotNull(dtOresponse);
        assertEquals("Uber", dtOresponse.getDescription());
        assertEquals(1L, dtOresponse.getMonthlyExpenseId());

        verify(repository).save(any(Expense.class));
    }
    @Test
    public void deveriaRetornarUmaDespesaPeloId(){
            MonthlyExpense monthlyExpense = new MonthlyExpense();
            monthlyExpense.setId(1L);

            Category category = new Category();
            category.setId(1L);

            Expense expense = new Expense();
            expense.setDescription("Uber");
            expense.setMonthlyExpense(monthlyExpense);
            expense.setCategory(category);

            ExpenseDTOrequest expenseDTOrequest = new ExpenseDTOrequest();
            expenseDTOrequest.setDescription("Uber");
            expenseDTOrequest.setMonthlyExpenseId(1L);
            expenseDTOrequest.setCategoryId(1L);

            when(monthlyExpenseRepository.findById(1L)).thenReturn(Optional.of(monthlyExpense));
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
            when(repository.save(any(Expense.class))).thenReturn(expense);

            ExpenseDTOresponse dtOresponse = service.insertExpense(expenseDTOrequest);

            assertNotNull(dtOresponse);
            assertEquals("Uber", dtOresponse.getDescription());
            assertEquals(1L, dtOresponse.getMonthlyExpenseId());

            verify(repository).save(any(Expense.class));
        }

    @Test
    public void deveriaRetornarTodasAsDespesas(){

        Category category1 = new Category();
        category1.setId(1L);
        Category category2 = new Category();
        category2.setId(1L);

        MonthlyExpense monthlyExpense = new MonthlyExpense();
        monthlyExpense.setId(1L);
        MonthlyExpense monthlyExpense2 = new MonthlyExpense();
        monthlyExpense.setId(2L);

        List<Expense> expenses = List.of(
                new Expense(1L, new BigDecimal("10.00"), "Uber", null, monthlyExpense, category1),
                new Expense(2L, new BigDecimal("24.00"), "Almoço", null, monthlyExpense2, category2)
        );
        when(repository.findAll()).thenReturn(expenses);

        List<ExpenseDTOresponse> expenseDTOresponseList = service.findAllExpenses();

        assertNotNull(expenseDTOresponseList);
        assertEquals(2, expenseDTOresponseList.size());
        assertEquals("Uber", expenseDTOresponseList.get(0).getDescription());
        assertEquals("Almoço", expenseDTOresponseList.get(1).getDescription());

        verify(repository).findAll();
    }
    @Test
    public void deveriaDeleterUmaDespesa(){
        service.deleteExpense(1L);

        verify(repository).deleteById(1L);
    }
}
