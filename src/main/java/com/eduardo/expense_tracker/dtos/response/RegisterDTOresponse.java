package com.eduardo.expense_tracker.dtos.response;

import com.eduardo.expense_tracker.entities.user.UserRole;
import lombok.Data;

@Data
public class RegisterDTOresponse{
    public Long id;
    public String email;
    public UserRole role;
}
