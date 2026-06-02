package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.ExpenseDTOrequest;
import com.eduardo.expense_tracker.dtos.response.ExpenseDTOresponse;
import com.eduardo.expense_tracker.entities.Expense;
import com.eduardo.expense_tracker.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<ExpenseDTOresponse>> findAll() {
        return ResponseEntity.ok().body(expenseService.findAllExpenses());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ExpenseDTOresponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(expenseService.findExpenseById(id));
    }
    @PostMapping(value = "/insert")
    public ResponseEntity<ExpenseDTOresponse> insertExpense(@RequestBody ExpenseDTOrequest expenseDTO) {
        ExpenseDTOresponse expense = expenseService.insertExpense(expenseDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(expense.getId()).toUri();
        return ResponseEntity.created(uri).body(expense);
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
