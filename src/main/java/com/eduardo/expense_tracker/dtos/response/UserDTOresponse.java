package com.eduardo.expense_tracker.dtos.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTOresponse {
    public String name;
    public String cpf;
    public String phone;
    public LocalDate birthDate;
    public Long locationId;
}