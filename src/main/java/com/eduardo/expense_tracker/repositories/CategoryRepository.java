package com.eduardo.expense_tracker.repositories;

import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
