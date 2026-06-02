package com.eduardo.expense_tracker.unit.service;

import com.eduardo.expense_tracker.repositories.BankAccountRepository;
import com.eduardo.expense_tracker.repositories.MonthlyExpenseRepository;
import com.eduardo.expense_tracker.services.MonthlyExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MonthlyExpenseTest {

    @Mock
    private MonthlyExpenseRepository repository;
    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private MonthlyExpenseService service;

    @Test
    public void deveriaInserirUmGastoMensal(){

    }

}
