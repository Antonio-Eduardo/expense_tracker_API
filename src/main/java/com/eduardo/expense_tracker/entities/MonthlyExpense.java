package com.eduardo.expense_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "tb_gasto_mensal")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal monthTotal;
    private Instant month;

    @ManyToOne
    @JoinColumn(name = "conta_bancaria_id")
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "monthlyExpense", cascade = CascadeType.ALL)
    private Set<Expense> expenses;
}
