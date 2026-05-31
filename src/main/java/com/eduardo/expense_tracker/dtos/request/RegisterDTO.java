package com.eduardo.expense_tracker.dtos.request;

import com.eduardo.expense_tracker.entities.user.UserRole;

public record RegisterDTO(String email, String password, UserRole role) {
}
