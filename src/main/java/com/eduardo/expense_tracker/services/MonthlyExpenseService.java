package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.repositories.BankAccountRepository;
import com.eduardo.expense_tracker.repositories.MonthlyExpenseRepository;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MonthlyExpenseService {

    @Autowired
    MonthlyExpenseRepository repository;
    @Autowired
    BankAccountRepository bankAccountRepository;

    public MonthlyExpense insertMonthlyExpense(MonthlyExpense monthlyExpense){
        return repository.save(monthlyExpense);
    }
    public MonthlyExpense findMonthlyExpenseById(Long id){
        return repository.findById(id).orElse(null);
    }
    public List<MonthlyExpense> findAllMonthlyExpenses(){
       return repository.findAll();
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
        public void updateData(MonthlyExpense monthlyExpenseFind, MonthlyExpense obj) {
            monthlyExpenseFind.setLimitExpense(obj.getLimitExpense());
        }
       public void processMonthlyExpense(MonthlyExpense monthlyExpense){
            BankAccount bankAccount = bankAccountRepository.findById(monthlyExpense.getBankAccount().getId())
                    .orElseThrow(() -> new ResourceNotFind("Bank Account not found with id: " + monthlyExpense.getBankAccount().getId()));
            BigDecimal updateBalance = bankAccount.getBalance().subtract(monthlyExpense.getMonthTotal());
            bankAccount.setBalance(updateBalance);
            bankAccountRepository.save(bankAccount);
        }

}
