package com.eduardo.expense_tracker.resource;

import com.eduardo.expense_tracker.entities.Expense;
import com.eduardo.expense_tracker.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseResource {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<Expense>> findAll() {
        return ResponseEntity.ok().body(expenseService.findAllExpenses());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Expense> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(expenseService.findExpenseById(id));
    }
    @PostMapping(value = "/insert")
    public ResponseEntity<Expense> insertExpense(@RequestBody Expense expense) {
        return ResponseEntity.ok().body(expenseService.insertExpense(expense));
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
