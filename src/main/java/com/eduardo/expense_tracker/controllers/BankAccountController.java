package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.BankAccountDTO;
import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/bank-account")
public class BankAccountController {

    @Autowired
    private BankAccountService service;

    @GetMapping
    public ResponseEntity<List<BankAccount>> findAll(){
        return ResponseEntity.ok().body(service.findAllBankAccounts());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BankAccount> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findBankAccountById(id));
    }
    @PostMapping(value = "/insert")
    public ResponseEntity<BankAccount> insertBankAccount(@RequestBody BankAccountDTO bankAccount) {
        BankAccount savedBankAccount = service.insertBankAccount(bankAccount);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedBankAccount.getId()).toUri();
        return ResponseEntity.created(uri).body(savedBankAccount);
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
        service.deleteBankAccount(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<BankAccount> updateBankAccount(@PathVariable Long id, @RequestBody BankAccount bankAccount) {
        service.updateBankAccount(id, bankAccount);
        return ResponseEntity.ok().body(service.findBankAccountById(id));
    }
}
