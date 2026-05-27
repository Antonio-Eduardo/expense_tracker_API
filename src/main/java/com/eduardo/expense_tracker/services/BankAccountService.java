package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.BankAccountDTO;
import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.repositories.BankAccountRepository;
import com.eduardo.expense_tracker.repositories.UserRepository;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository repository;

    @Autowired
    private UserRepository userRepository;

    public BankAccount insertBankAccount(BankAccountDTO bankAccount){
        BankAccount bankAccountDB = new BankAccount();
        User user = userRepository.findById(bankAccount.getUserId()).orElseThrow(() -> new ResourceNotFind("User not found with id: " + bankAccount.getUserId()));
        bankAccountDB.setTypeAccount(bankAccount.getTypeAccount());
        bankAccountDB.setCreditCardClosingDate(bankAccount.getCreditCardClosingDate());
        bankAccountDB.setBalance(bankAccount.getBalance());
        bankAccountDB.setUser(user);

        return repository.save(bankAccountDB);
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
        BankAccount bankAccountFind = repository.findById(id).orElseThrow(() -> new ResourceNotFind("Bank Account not found with id: " + id));
            updateData(bankAccountFind, obj);
            repository.save(bankAccountFind);
    }
}
