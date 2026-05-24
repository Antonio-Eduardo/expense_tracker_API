package com.eduardo.expense_tracker.repositories;

import com.eduardo.expense_tracker.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<BankAccount,Long> {
}
