package com.eduardo.expense_tracker.repositories;

import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
