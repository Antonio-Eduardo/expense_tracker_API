package com.eduardo.expense_tracker;

import com.eduardo.expense_tracker.dtos.BankAccountDTO;
import com.eduardo.expense_tracker.dtos.UserDTO;
import com.eduardo.expense_tracker.entities.*;
import com.eduardo.expense_tracker.repositories.*;
import com.eduardo.expense_tracker.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

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
	void deveriaDescontarDoMensalEBanco() {
		Category category = new Category();
		category.setName("Alimentação");
		category.setNotifyLimit(new BigDecimal(200));
		categoryServices.insertCategory(category);

		Location location = new Location();
		location.setCity("São Paulo");
		location.setState("SP");
		location.setAddress1("Rua A, 123");
		location.setAddress2("Apto 45");
		location.setZipCode("12345-678");
		locationService.insertLocation(location);

		UserDTO userDTO = new UserDTO();
		userDTO.setName("Eduardo");
		userDTO.setEmail("eduardo@gmail.com");
		userDTO.setPassword("123456");
		userDTO.setCpf("12345678900");
		userDTO.setPhone("11999999999");
		userDTO.setBirthDate(LocalDate.parse("2003-06-18"));
		userDTO.setLocationId(location.getId());
		userService.insertUser(userDTO);
		User user = userService.userFindById(1L);

		BankAccountDTO bankAccount = new BankAccountDTO();
		bankAccount.setBalance(new BigDecimal(1000));
		bankAccount.setTypeAccount("Caixa");
		bankAccount.setCreditCardClosingDate(Instant.parse("2026-05-29T00:00:00Z"));
		bankAccount.setUserId(user.getId());
		bankAccountService.insertBankAccount(bankAccount);
		BankAccount bankAccountDB = bankAccountService.findBankAccountById(1L);

		MonthlyExpense monthlyExpense = new MonthlyExpense();
		monthlyExpense.setMonthTotal(new BigDecimal(0));
		monthlyExpense.setMonth("Maio");
		monthlyExpense.setLimitExpense(new BigDecimal(500));
		monthlyExpense.setBankAccount(bankAccountDB);
		monthlyExpenseService.insertMonthlyExpense(monthlyExpense);

		Expense expense = new Expense();
		expense.setAmount(new BigDecimal(100));
		expense.setDescription("Compra no supermercado");
		expense.setExpenseMoment(Instant.now());
		expense.setMonthlyExpense(monthlyExpense);
		expense.setCategory(category);
		expenseService.insertExpense(expense);
		expenseService.processExpense(expense);

		Expense expense1 = new Expense();
		expense1.setAmount(new BigDecimal(150));
		expense1.setDescription("Compra no mercado");
		expense1.setExpenseMoment(Instant.now());
		expense1.setMonthlyExpense(monthlyExpense);
		expense1.setCategory(category);
		expenseService.insertExpense(expense1);
		expenseService.processExpense(expense1);

		monthlyExpenseService.processMonthlyExpense(monthlyExpense);
	}

}
