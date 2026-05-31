package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.request.ExpenseDTO;
import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.entities.Expense;
import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.repositories.CategoryRepository;
import com.eduardo.expense_tracker.repositories.ExpenseRepository;
import com.eduardo.expense_tracker.repositories.MonthlyExpenseRepository;
import com.eduardo.expense_tracker.services.exceptions.BusinessException;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repository;
    @Autowired
    private MonthlyExpenseRepository monthlyExpenseRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    public ExpenseDTO insertExpense(ExpenseDTO expenseDTO){
        Expense expenseDB = new Expense();

        MonthlyExpense monthlyExpense = monthlyExpenseRepository.findById(expenseDTO.getMonthlyExpenseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Monthly Expense not found with id: "
                        + expenseDTO.getMonthlyExpenseId()));

        Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada"
                        + expenseDTO.getCategoryId()));

        expenseDB.setAmount(expenseDTO.getAmount());
        expenseDB.setDescription(expenseDTO.getDescription());
        expenseDB.setExpenseMoment(Instant.now());
        expenseDB.setMonthlyExpense(monthlyExpense);
        expenseDB.setCategory(category);

        repository.save(expenseDB);

        ExpenseDTO response = new ExpenseDTO();
        response.setDescription(expenseDB.getDescription());
        response.setAmount(expenseDB.getAmount());
        response.setExpenseMoment(expenseDB.getExpenseMoment());
        response.setMonthlyExpenseId(expenseDB.getMonthlyExpense().getId());
        response.setCategoryId(expenseDB.getCategory().getId());
        return response;
    }


     public Expense findExpenseById(Long id){
         return repository.findById(id).orElse(null);
     }

     public List<Expense> findAllExpenses(){
         return repository.findAll();
     }

     public void deleteExpense(Long id) {
         repository.deleteById(id);
     }

     public void processExpense(Expense expense) {
         MonthlyExpense monthlyExpense = monthlyExpenseRepository.findById(expense.getMonthlyExpense().getId()).orElseThrow(() -> new ResourceNotFoundException("Monthly Expense not found with id: " + expense.getMonthlyExpense().getId()));
         if (expense.getAmount().compareTo(monthlyExpense.getLimitExpense()) > 0) {
             throw new BusinessException("Expense amount exceeds the monthly limit.");
         }
             BigDecimal updateTotal = monthlyExpense.getMonthTotal().add(expense.getAmount());
             monthlyExpense.setMonthTotal(updateTotal);

             BigDecimal updateLimit = monthlyExpense.getLimitExpense().subtract(expense.getAmount());
             monthlyExpense.setLimitExpense(updateLimit);
             monthlyExpenseRepository.save(monthlyExpense);
     }
}
