package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.request.BankAccountDTOrequest;
import com.eduardo.expense_tracker.dtos.response.BankAccountDTOresponse;
import com.eduardo.expense_tracker.dtos.response.UserDTOresponse;
import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.repositories.BankAccountRepository;
import com.eduardo.expense_tracker.repositories.UserRepository;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository repository;

    @Autowired
    private UserRepository userRepository;

    public BankAccountDTOresponse insertBankAccount(BankAccountDTOrequest bankAccount){
        BankAccount bankAccountDB = new BankAccount();
        User user = userRepository.findById(bankAccount.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + bankAccount.getUserId()));
        bankAccountDB.setTypeAccount(bankAccount.getTypeAccount());
        bankAccountDB.setCreditCardClosingDate(bankAccount.getCreditCardClosingDate());
        bankAccountDB.setBalance(bankAccount.getBalance());
        bankAccountDB.setUser(user);

        bankAccountDB = repository.save(bankAccountDB);

        return convertToBankAccountResponseDTO(bankAccountDB);
    }
    public BankAccountDTOresponse findBankAccountById(Long id){
        BankAccount bk = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException
                        ("Bank Account not found" + id));
        return convertToBankAccountResponseDTO(bk);
    }

    public List<BankAccountDTOresponse> findAllBankAccounts(){

        return repository.findAll().stream().map(this::convertToBankAccountResponseDTO).toList();
    }

    public void deleteBankAccount(Long id) {
        repository.deleteById(id);
    }

    public void updateData(BankAccount bankAccountFind, BankAccountDTOrequest obj){
        if (obj != null) {
            bankAccountFind.setTypeAccount(obj.getTypeAccount());
        }
        if (obj != null) {
            bankAccountFind.setCreditCardClosingDate(obj.getCreditCardClosingDate());
        }
    }

    public BankAccountDTOresponse updateBankAccount(Long id, BankAccountDTOrequest obj){
        BankAccount bankAccountFind = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bank Account not found with id: " + id));
            updateData(bankAccountFind, obj);

            bankAccountFind = repository.save(bankAccountFind);
           return convertToBankAccountResponseDTO(bankAccountFind);
    }
    public BankAccountDTOresponse convertToBankAccountResponseDTO(BankAccount bk){
        BankAccountDTOresponse response = new BankAccountDTOresponse();
        if (bk.getId() != null) {
            response.setId(bk.getId());
        }
        if (bk.getTypeAccount() != null) {
            response.setTypeAccount(bk.getTypeAccount());
        }
        if (bk.getBalance() != null) {
            response.setBalance(bk.getBalance());
        }
        if (bk.getUser() != null) {
            response.setUserId(bk.getUser().getId());
        }
        if (bk.getCreditCardClosingDate() != null) {
            response.setCreditCardClosingDate(bk.getCreditCardClosingDate());
        }
        return response;
    }
}
