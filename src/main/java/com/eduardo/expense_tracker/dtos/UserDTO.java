package com.eduardo.expense_tracker.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    public String name;
    public String cpf;
    public String phone;
    public LocalDate birthDate;
    public Long locationId;
}