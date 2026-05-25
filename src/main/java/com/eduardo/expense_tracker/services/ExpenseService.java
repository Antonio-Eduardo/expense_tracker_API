package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.entities.Expense;
import com.eduardo.expense_tracker.repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repository;

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
}
