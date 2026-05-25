package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BankAccountService {

    @Autowired
    BankAccountRepository repository;

    public BankAccount insertBankAccount(BankAccount bankAccount){
        return repository.save(bankAccount);
    }
    public BankAccount findBankAccountById(Long id){
        return repository.findById(id).orElse(null);
    }
    public List<BankAccount> findAllBankAccounts(){
        return repository.findAll();
    }
    public void deleteBankAccount(Long id) {
        repository.deleteById(id);
    }
    public void updateData(BankAccount bankAccountFind, BankAccount obj){
        bankAccountFind.setTypeAccount(obj.getTypeAccount());
        bankAccountFind.setCreditCardClosingDate(obj.getCreditCardClosingDate());
    }
    public void updateBankAccount(Long id, BankAccount obj){
        BankAccount bankAccountFind = repository.findById(id).orElse(null);
        if (bankAccountFind != null) {
            updateData(bankAccountFind, obj);
            repository.save(bankAccountFind);
        }
    }
}
