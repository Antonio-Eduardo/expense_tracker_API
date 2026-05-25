package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.repositories.MonthlyExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthlyExpenseService {

    @Autowired
    MonthlyExpenseRepository repository;

    public MonthlyExpense insertMonthlyExpense(MonthlyExpense monthlyExpense){
        return repository.save(monthlyExpense);
    }
    public MonthlyExpense findMonthlyExpenseById(Long id){
        return repository.findById(id).orElse(null);
    }
    public void deleteMonthlyExpense(Long id) {
        repository.deleteById(id);
    }
        public MonthlyExpense updateMonthlyExpense(Long id, MonthlyExpense obj){
            MonthlyExpense monthlyExpenseFind = repository.findById(id).orElse(null);
            if (monthlyExpenseFind != null) {
                updateData(monthlyExpenseFind, obj);
                return repository.save(monthlyExpenseFind);
            }
            return null;
        }
        private void updateData(MonthlyExpense monthlyExpenseFind, MonthlyExpense obj) {
            monthlyExpenseFind.setLimitExpense(obj.getLimitExpense());
        }
}
