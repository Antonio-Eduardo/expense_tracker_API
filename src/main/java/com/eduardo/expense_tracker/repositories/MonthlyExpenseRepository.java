package com.eduardo.expense_tracker.repositories;

import com.eduardo.expense_tracker.entities.MonthlyExpense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyExpenseRepository extends JpaRepository<MonthlyExpense,Long> {
}
