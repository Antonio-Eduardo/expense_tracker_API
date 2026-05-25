package com.eduardo.expense_tracker.configs;

import com.eduardo.expense_tracker.entities.*;
import com.eduardo.expense_tracker.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Configuration
public class InitialData {

    @Bean
    CommandLineRunner run(
            UserRepository userRepository,
            LocationRepository locationRepository,
            BankAccountRepository bankAccountRepository,
            CategoryRepository categoryRepository,
            MonthlyExpenseRepository monthlyExpenseRepository,
            ExpenseRepository expenseRepository
    ) {
        return args -> {

            Location location = new Location();
            location.setCity("Fortaleza");
            location.setState("CE");
            location.setAddress1("Rua das Flores, 100");
            location.setAddress2("Apto 202");
            location.setZipCode("60000-000");
            locationRepository.save(location);

            User user = new User();
            user.setName("Eduardo");
            user.setEmail("eduardo@gmail.com");
            user.setPassword("123456");
            user.setCpf("12345678900");
            user.setPhone("85999999999");
            user.setBirthDate(LocalDate.of(2003, 6, 18));
            user.setLocation(location);
            userRepository.save(user);

            BankAccount account = new BankAccount();
            account.setTypeAccount("CORRENTE");
            account.setBalance(new BigDecimal("2000.00"));
            account.setCreditCardClosingDate(Instant.now());
            account.setUser(user);
            bankAccountRepository.save(account);

            Category food = new Category();
            food.setName("Alimentação");
            food.setNotifyLimit(new BigDecimal("500"));
            categoryRepository.save(food);

            Category transport = new Category();
            transport.setName("Transporte");
            transport.setNotifyLimit(new BigDecimal("300"));
            categoryRepository.save(transport);

            MonthlyExpense monthly = new MonthlyExpense();
            monthly.setMonth("MAIO");
            monthly.setMonthTotal(BigDecimal.ZERO);
            monthly.setLimitExpense(new BigDecimal("1000"));
            monthly.setBankAccount(account);
            monthlyExpenseRepository.save(monthly);

            Expense e1 = new Expense();
            e1.setAmount(new BigDecimal("120"));
            e1.setDescription("Mercado");
            e1.setExpenseMoment(Instant.now());
            e1.setCategory(food);
            e1.setMonthlyExpense(monthly);
            expenseRepository.save(e1);

            Expense e2 = new Expense();
            e2.setAmount(new BigDecimal("80"));
            e2.setDescription("Uber");
            e2.setExpenseMoment(Instant.now());
            e2.setCategory(transport);
            e2.setMonthlyExpense(monthly);
            expenseRepository.save(e2);

            System.out.println("sucess");
        };
    }
}