package com.eduardo.expense_tracker;

import com.eduardo.expense_tracker.entities.Location;
import com.eduardo.expense_tracker.entities.User;
import com.eduardo.expense_tracker.repositories.*;
import com.eduardo.expense_tracker.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
class ExpenseTrackerApplicationTests {

	@Autowired
	private BankAccountRepository bankAccountRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MonthlyExpenseRepository monthlyExpenseRepository;
	@Autowired
	private ExpenseRepository expenseRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private CategoryServices categoryServices;
	@Autowired
	private MonthlyExpenseService monthlyExpenseService;

	@BeforeEach
	void limpar(){
		expenseRepository.deleteAll();
		monthlyExpenseRepository.deleteAll();
		locationRepository.deleteAll();
		categoryRepository.deleteAll();
		bankAccountRepository.deleteAll();
		userRepository.deleteAll();
	}
	@Test
	void deveriaCriarUser() {

	}

}
