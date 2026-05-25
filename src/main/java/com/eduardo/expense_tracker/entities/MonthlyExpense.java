package com.eduardo.expense_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_monthly_expense")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal monthTotal;
    private String month;

    private BigDecimal limitExpense;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "monthlyExpense", cascade = CascadeType.ALL)
    private Set<Expense> expenses;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MonthlyExpense that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
