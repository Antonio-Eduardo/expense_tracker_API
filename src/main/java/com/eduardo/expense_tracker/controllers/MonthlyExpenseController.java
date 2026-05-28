package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.MonthlyExpenseDTO;
import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.services.MonthlyExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("month")
public class MonthlyExpenseController {

    @Autowired
    MonthlyExpenseService monthlyExpenseService;

    @GetMapping
    public ResponseEntity<List<MonthlyExpense>> findAll() {
        return ResponseEntity.ok().body(monthlyExpenseService.findAllMonthlyExpenses());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MonthlyExpense> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(monthlyExpenseService.findMonthlyExpenseById(id));
    }
    @PostMapping(value = "/insert")
    public ResponseEntity<MonthlyExpense> insertMonthlyExpense(@RequestBody MonthlyExpenseDTO monthlyExpenseDTO) {
        MonthlyExpense monthlyExpense = monthlyExpenseService.insertMonthlyExpense(monthlyExpenseDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(monthlyExpense.getId()).toUri();
        return ResponseEntity.created(uri).body(monthlyExpense);
    }
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<MonthlyExpense> updateMonthlyExpense(@PathVariable Long id, @RequestBody MonthlyExpense monthlyExpense) {
        return ResponseEntity.ok().body(monthlyExpenseService.updateMonthlyExpense(id, monthlyExpense));
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteMonthlyExpense(@PathVariable Long id) {
        monthlyExpenseService.deleteMonthlyExpense(id);
        return ResponseEntity.noContent().build();
    }
}
