package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.MonthlyExpenseDTOrequest;
import com.eduardo.expense_tracker.dtos.response.MonthlyExpenseDTOresponse;
import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.services.MonthlyExpenseService;
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
@RequestMapping("month")
@Tag(name = "Monthly Expenses", description = "Operações relacionadas às Despesas Mensais")
public class MonthlyExpenseController {

    @Autowired
    MonthlyExpenseService monthlyExpenseService;

    @GetMapping
    @Operation(summary = "Lista todas as despesas mensais")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<List<MonthlyExpenseDTOresponse>> findAll() {
        return ResponseEntity.ok().body(monthlyExpenseService.findAllMonthlyExpenses());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Listar despesa mensal por Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Despesa mensal não encontrada")
    public ResponseEntity<MonthlyExpenseDTOresponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(monthlyExpenseService.findMonthlyExpenseById(id));
    }
    @PostMapping(value = "/insert")
    @Operation(summary = "Insere uma nova despesa mensal")
    @ApiResponse(responseCode = "201", description = "Despesa mensal criada com sucesso")
    public ResponseEntity<MonthlyExpenseDTOresponse> insertMonthlyExpense(@RequestBody MonthlyExpenseDTOrequest monthlyExpenseDTO) {
        MonthlyExpenseDTOresponse monthlyExpense = monthlyExpenseService.insertMonthlyExpense(monthlyExpenseDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(monthlyExpense.getId()).toUri();
        return ResponseEntity.created(uri).body(monthlyExpense);
    }
    @PutMapping(value = "/update/{id}")
    @Operation(summary = "Atualiza uma despesa mensal")
    @ApiResponse(responseCode = "200", description = "Despesa mensal atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Despesa mensal não encontrada")
    public ResponseEntity<MonthlyExpenseDTOresponse> updateMonthlyExpense(@PathVariable Long id, @RequestBody MonthlyExpenseDTOrequest monthlyExpense) {
        return ResponseEntity.ok().body(monthlyExpenseService.updateMonthlyExpense(id, monthlyExpense));
    }
    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Exclui uma despesa mensal")
    @ApiResponse(responseCode = "204", description = "Despesa mensal excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Despesa mensal não encontrada")
    public ResponseEntity<Void> deleteMonthlyExpense(@PathVariable Long id) {
        monthlyExpenseService.deleteMonthlyExpense(id);
        return ResponseEntity.noContent().build();
    }
}
