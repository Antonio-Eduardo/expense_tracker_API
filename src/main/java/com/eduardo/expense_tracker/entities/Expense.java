package com.eduardo.expense_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tb_expense")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private String description;
    private Instant expenseMoment;

    @ManyToOne
    @JoinColumn(name = "gasto_mensal_id")
    private MonthlyExpense monthlyExpense;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
