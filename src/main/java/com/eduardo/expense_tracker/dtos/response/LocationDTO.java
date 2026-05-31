package com.eduardo.expense_tracker.dtos.response;

import lombok.Data;

@Data
public class LocationDTO {
    private String city;
    private String state;
    private String address1;
    private String address2;
    private String zipCode;
}
