package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.request.MonthlyExpenseDTOrequest;
import com.eduardo.expense_tracker.dtos.response.MonthlyExpenseDTOresponse;
import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.repositories.BankAccountRepository;
import com.eduardo.expense_tracker.repositories.MonthlyExpenseRepository;
import com.eduardo.expense_tracker.services.exceptions.BusinessException;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public MonthlyExpenseDTOresponse insertMonthlyExpense(MonthlyExpenseDTOrequest monthlyExpenseDTO){
        MonthlyExpense monthlyExpenseDB = new MonthlyExpense();
        BankAccount bankAccount = bankAccountRepository.findById(monthlyExpenseDTO.getBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found with id: " + monthlyExpenseDTO.getBankAccountId()));
        monthlyExpenseDB.setLimitExpense(monthlyExpenseDTO.getLimitExpense());
        monthlyExpenseDB.setBankAccount(bankAccount);
        monthlyExpenseDB.setMonthTotal(monthlyExpenseDTO.getMonthTotal());
        monthlyExpenseDB.setMonth(monthlyExpenseDTO.getMonth());
        monthlyExpenseDB = repository.save(monthlyExpenseDB);
        return convertDTOresponse(monthlyExpenseDB);
    }

    public MonthlyExpenseDTOresponse findMonthlyExpenseById(Long id){
       MonthlyExpense monthlyExpense = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Monthly Expense not found with id: " + id));
        return convertDTOresponse(monthlyExpense);
    }

    public List<MonthlyExpenseDTOresponse> findAllMonthlyExpenses(){
       return repository.findAll().stream().map(this::convertDTOresponse).toList();
    }

    @Transactional
    public void deleteMonthlyExpense(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public MonthlyExpenseDTOresponse updateMonthlyExpense(Long id, MonthlyExpense obj){
            MonthlyExpense monthlyExpenseFind = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Monthly Expense not found with id: " + id));
                updateData(monthlyExpenseFind, obj);
                monthlyExpenseFind = repository.save(monthlyExpenseFind);
                return convertDTOresponse(monthlyExpenseFind);
        }
    public void updateData(MonthlyExpense monthlyExpenseFind, MonthlyExpense obj) {
            monthlyExpenseFind.setLimitExpense(obj.getLimitExpense());
        }
    @Transactional
    public void processMonthlyExpense(MonthlyExpense monthlyExpense){
            BankAccount bankAccount = bankAccountRepository.findById(monthlyExpense.getBankAccount().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found with id: " + monthlyExpense.getBankAccount().getId()));
            if (bankAccount.getBalance().compareTo(monthlyExpense.getMonthTotal()) < 0) {
                throw new BusinessException("Insufficient balance in the bank account to cover the monthly expense.");
            } else {
                BigDecimal updateBalance = bankAccount.getBalance().subtract(monthlyExpense.getMonthTotal());
                bankAccount.setBalance(updateBalance);
                bankAccountRepository.save(bankAccount);
            }
        }

        public MonthlyExpenseDTOresponse convertDTOresponse(MonthlyExpense monthlyExpense) {
            MonthlyExpenseDTOresponse medr = new MonthlyExpenseDTOresponse();
            medr.setId(monthlyExpense.getId());
            medr.setLimitExpense(monthlyExpense.getLimitExpense());
            medr.setMonthTotal(monthlyExpense.getMonthTotal());
            medr.setMonth(monthlyExpense.getMonth());
            if (monthlyExpense.getBankAccount() != null) {
                medr.setBankAccountId(monthlyExpense.getBankAccount().getId());
            }
            return medr;
        }
    }
