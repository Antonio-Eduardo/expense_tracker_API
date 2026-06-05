package com.eduardo.expense_tracker.unit.service;

import com.eduardo.expense_tracker.dtos.request.MonthlyExpenseDTOrequest;
import com.eduardo.expense_tracker.dtos.response.MonthlyExpenseDTOresponse;
import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.repositories.BankAccountRepository;
import com.eduardo.expense_tracker.repositories.MonthlyExpenseRepository;
import com.eduardo.expense_tracker.services.MonthlyExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        MonthlyExpense monthlyExpense = new MonthlyExpense();
        monthlyExpense.setMonth("Junho");
        monthlyExpense.setBankAccount(bankAccount);

        MonthlyExpenseDTOrequest monthlyExpenseDTOrequest = new MonthlyExpenseDTOrequest();
        monthlyExpenseDTOrequest.setMonth("Junho");
        monthlyExpenseDTOrequest.setBankAccountId(1L);

        when(bankAccountRepository.findById(any(Long.class))).thenReturn(Optional.of(bankAccount));
        when(repository.save(any(MonthlyExpense.class))).thenReturn(monthlyExpense);

        MonthlyExpenseDTOresponse result = service.insertMonthlyExpense(monthlyExpenseDTOrequest);

        assertNotNull(result);
        assertEquals("Junho", result.getMonth());
        assertEquals(1L, result.getBankAccountId());

        verify(repository).save(any(MonthlyExpense.class));
    }
    @Test
    public void deveriaAcharUmaContaPorId(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        MonthlyExpense monthlyExpense = new MonthlyExpense();
        monthlyExpense.setId(1L);
        monthlyExpense.setBankAccount(bankAccount);

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(monthlyExpense));

        MonthlyExpenseDTOresponse result = service.findMonthlyExpenseById(monthlyExpense.getId());

        assertEquals(1L, result.getId());
        assertNotNull(result);
        assertEquals(1L, result.getBankAccountId());

        verify(repository).findById(any(Long.class));
    }
    @Test
    public void deveriaDeleterUmaContaPorId(){
        service.deleteMonthlyExpense(1L);

        verify(repository).deleteById(any(Long.class));
    }
    @Test
    public void deveriaRetornarTodosOsGastosMensais(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        MonthlyExpense monthlyExpense1 = new MonthlyExpense();
        monthlyExpense1.setId(1L);
        monthlyExpense1.setBankAccount(bankAccount);
        MonthlyExpense monthlyExpense2 = new MonthlyExpense();
        monthlyExpense2.setId(2L);
        monthlyExpense2.setBankAccount(bankAccount);

        when(repository.findAll()).thenReturn(List.of(monthlyExpense1, monthlyExpense2));

        List<MonthlyExpenseDTOresponse> result = service.findAllMonthlyExpenses();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(repository).findAll();
    }
    @Test
    public void deveriaAtualizarUmGastoMensal(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        MonthlyExpense monthlyExpense = new MonthlyExpense();
        monthlyExpense.setId(1L);
        monthlyExpense.setLimitExpense(new BigDecimal("1000.00"));
        monthlyExpense.setBankAccount(bankAccount);

        MonthlyExpenseDTOrequest novoRequest = new MonthlyExpenseDTOrequest();
        novoRequest.setLimitExpense(new BigDecimal("1500.00"));

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(monthlyExpense));
        when(repository.save(any(MonthlyExpense.class))).thenReturn(monthlyExpense);

        MonthlyExpenseDTOresponse dtOresponse = service.updateMonthlyExpense(monthlyExpense.getId(), novoRequest);

        assertNotNull(dtOresponse);
        assertEquals(new BigDecimal("1500.00"), dtOresponse.getLimitExpense());

        verify(repository).findById(any(Long.class));
        verify(repository).save(any(MonthlyExpense.class));
    }
}
