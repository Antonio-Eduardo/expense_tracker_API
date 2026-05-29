package com.eduardo.expense_tracker.unit.service;

import com.eduardo.expense_tracker.dtos.BankAccountDTO;
import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.repositories.BankAccountRepository;
import com.eduardo.expense_tracker.services.BankAccountService;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @Test
    public void deveriaInserirContaBancaria(){
        User user = new User();
        user.setId(1L);

        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setTypeAccount("Conta Corrente");
        bankAccountDTO.setUserId(1L);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setTypeAccount("Conta Corrente");
        bankAccount.setUser(user);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);

        BankAccount result = bankAccountService.insertBankAccount(bankAccountDTO);

        assertNotNull(result);
        assertEquals("Conta Corrente", result.getTypeAccount());
        verify(bankAccountRepository).save(any(BankAccount.class));
    }
    @Test
    public void deveriaRetornarContaBancarioPeloId(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);

        when(bankAccountRepository.findById(any(Long.class))).thenReturn(Optional.of(bankAccount));

        BankAccount result = bankAccountService.findBankAccountById(bankAccount.getId());

        assertNotNull(result);
        assertEquals(1L,result.getId());

        verify(bankAccountRepository).findById(any(Long.class));
    }
    @Test
    public void deveriaRetornarTodasAsContasBancarias(){
        List<BankAccount> bankAccounts = List.of(new BankAccount(), new BankAccount());

        when(bankAccountRepository.findAll()).thenReturn(bankAccounts);

        List<BankAccount> result = bankAccountService.findAllBankAccounts();

        assertNotNull(result);
        assertEquals(2,result.size());

        verify(bankAccountRepository).findAll();
    }
    @Test
    public void deveriaDeleterUmaContaBancariaPeloId(){
        bankAccountService.deleteBankAccount(1L);

        verify(bankAccountRepository).deleteById(1L);
    }
    @Test
    public void deveriaAtualizarUmaContaBancariaPeloId(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setTypeAccount("antigo");
        bankAccount.setId(1L);
        bankAccount.setCreditCardClosingDate(Instant.parse("2026-05-29T10:15:30Z"));

        BankAccount bankAccountdto = new BankAccount();
        bankAccount.setTypeAccount("novo");

        when(bankAccountRepository.findById(any(Long.class))).thenReturn(Optional.of(bankAccount));
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);

        BankAccount result = bankAccountService.updateBankAccount(bankAccount.getId(),bankAccountdto);

        assertNotNull(result);
        assertEquals("novo", result.getTypeAccount());

        verify(bankAccountRepository).findById(any(Long.class));
        verify(bankAccountRepository).save(any(BankAccount.class));

    }
}
