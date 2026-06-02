package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.ExpenseDTOrequest;
import com.eduardo.expense_tracker.dtos.response.ExpenseDTOresponse;
import com.eduardo.expense_tracker.entities.Expense;
import com.eduardo.expense_tracker.services.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/expense")
@Tag(name = "Expenses", description = "Operações relacionadas às Despesas")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    @Operation(summary = "Lista todas as despesas")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<List<ExpenseDTOresponse>> findAll() {
        return ResponseEntity.ok().body(expenseService.findAllExpenses());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Listar despesa por Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    public ResponseEntity<ExpenseDTOresponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(expenseService.findExpenseById(id));
    }
    @PostMapping(value = "/insert")
    @Operation(summary = "Insere uma nova despesa")
    @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados da despesa inválidos")
    public ResponseEntity<ExpenseDTOresponse> insertExpense(@RequestBody ExpenseDTOrequest expenseDTO) {
        ExpenseDTOresponse expense = expenseService.insertExpense(expenseDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(expense.getId()).toUri();
        return ResponseEntity.created(uri).body(expense);
    }
    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Exclui uma despesa")
    @ApiResponse(responseCode = "204", description = "Despesa excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
