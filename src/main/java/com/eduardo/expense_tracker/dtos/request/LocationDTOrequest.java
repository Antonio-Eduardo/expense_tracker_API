package com.eduardo.expense_tracker.dtos.request;

import lombok.Data;

@Data
public class LocationDTOrequest {
    private String city;
    private String state;
    private String address1;
    private String address2;
    private String zipCode;
}
