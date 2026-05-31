package com.eduardo.expense_tracker.dtos.response;

import com.eduardo.expense_tracker.entities.user.UserRole;

public record RegisterDTOresponse(String email, String password, UserRole role) {
}
