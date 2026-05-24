package com.eduardo.expense_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "tb_bank_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;
    private String typeAccount;
    private Instant creditCardClosingDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL)
    private Set<MonthlyExpense> monthlyExpense;
}
