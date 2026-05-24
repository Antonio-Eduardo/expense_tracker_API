package com.eduardo.expense_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal expenseLimit;
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private java.util.Set<Expense> expenses;
}
