package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.entities.Expense;
import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.repositories.ExpenseRepository;
import com.eduardo.expense_tracker.repositories.MonthlyExpenseRepository;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repository;
    @Autowired
    private MonthlyExpenseRepository monthlyExpenseRepository;


    public void insertExpense(Expense expense){
            repository.save(expense);
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
         MonthlyExpense monthlyExpense = monthlyExpenseRepository.findById(expense.getMonthlyExpense().getId()).orElseThrow(() -> new ResourceNotFind("Monthly Expense not found with id: " + expense.getMonthlyExpense().getId()));
             BigDecimal updateTotal = monthlyExpense.getMonthTotal().add(expense.getAmount());
             monthlyExpense.setMonthTotal(updateTotal);

             BigDecimal updateLimit = monthlyExpense.getLimitExpense().subtract(expense.getAmount());
             monthlyExpense.setLimitExpense(updateLimit);
             monthlyExpenseRepository.save(monthlyExpense);
     }
}
