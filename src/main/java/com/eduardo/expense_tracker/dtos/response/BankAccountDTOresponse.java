package com.eduardo.expense_tracker.dtos.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class BankAccountDTOresponse {
    private BigDecimal balance;
    private String typeAccount;
    private Instant creditCardClosingDate;
    private Long userId;

}
