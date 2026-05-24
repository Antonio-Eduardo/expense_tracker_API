package com.eduardo.expense_tracker.repositories;

import com.eduardo.expense_tracker.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Location,Long> {
}
