package com.eduardo.expense_tracker.repositories;

import com.eduardo.expense_tracker.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
   Optional<User> findByEmail(String email);
}
