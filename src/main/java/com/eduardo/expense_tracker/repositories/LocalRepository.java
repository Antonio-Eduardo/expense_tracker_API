package com.eduardo.expense_tracker.repositories;

import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.entities.Local;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local,Long> {
}
