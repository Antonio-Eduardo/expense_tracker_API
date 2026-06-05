package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.BankAccountDTOrequest;
import com.eduardo.expense_tracker.dtos.response.BankAccountDTOresponse;
import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.services.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/bank-account")
@Tag(name = "Bank Accounts", description = "Operações relacionadas às Contas Bancárias")
public class BankAccountController {

    @Autowired
    private BankAccountService service;

    @GetMapping
    @Operation(summary = "Lista todas as contas bancárias")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<List<BankAccountDTOresponse>> findAll(){
        return ResponseEntity.ok().body(service.findAllBankAccounts());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Listar conta bancária por Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Conta bancária não encontrada")
    public ResponseEntity<BankAccountDTOresponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findBankAccountById(id));
    }
    @PostMapping(value = "/insert")
    @Operation(summary = "Insere uma nova conta bancária")
    @ApiResponse(responseCode = "201", description = "Conta bancária criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados da conta bancária inválidos")
    public ResponseEntity<BankAccountDTOresponse> insertBankAccount(@RequestBody BankAccountDTOrequest bankAccount) {
        BankAccountDTOresponse savedBankAccount = service.insertBankAccount(bankAccount);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedBankAccount.getId()).toUri();
        return ResponseEntity.created(uri).body(savedBankAccount);
    }
    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Exclui uma conta bancária")
    @ApiResponse(responseCode = "204", description = "Conta bancária excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta bancária não encontrada")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
        service.deleteBankAccount(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping(value = "/update/{id}")
    @Operation(summary = "Atualiza uma conta bancária")
    @ApiResponse(responseCode = "200", description = "Conta bancária atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta bancária não encontrada")
    public ResponseEntity<BankAccountDTOresponse> updateBankAccount(@PathVariable Long id, @RequestBody BankAccountDTOrequest bankAccount) {
        service.updateBankAccount(id, bankAccount);
        return ResponseEntity.ok().body(service.findBankAccountById(id));
    }
}
