package com.eduardo.expense_tracker.integration;

import com.eduardo.expense_tracker.TestcontainersConfiguration;
import com.eduardo.expense_tracker.dtos.request.*;
import com.eduardo.expense_tracker.entities.*;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.entities.user.UserRole;
import com.eduardo.expense_tracker.repositories.*;
import com.eduardo.expense_tracker.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;


@Import(TestcontainersConfiguration.class)
@Disabled("desativar por enquanto, notebook sem docker")
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
	private CategoryService categoryServices;
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
		RegisterDTO registerDTO = new RegisterDTO("eduardo@gmail.com", "password", UserRole.USER);
		userService.createUser(registerDTO);
		User userfound = userService.findByEmail(registerDTO.email());

		Category category = new Category();
		category.setName("Alimentação");
		category.setNotifyLimit(new BigDecimal(200));
		categoryServices.insertCategory(category);

		LocationDTO location = new LocationDTO();
		location.setCity("São Paulo");
		location.setState("SP");
		location.setAddress1("Rua A, 123");
		location.setAddress2("Apto 45");
		location.setZipCode("12345-678");
		locationService.insertLocation(location);

		UserDTO userDTO = new UserDTO();
		userDTO.setName("Eduardo");
		userDTO.setCpf("12345678900");
		userDTO.setPhone("11999999999");
		userDTO.setBirthDate(LocalDate.parse("2003-06-18"));
		//userDTO.setLocationId(location.getId());
		userService.updateUser(userfound.getId(), userDTO);
		User user = userService.userFindById(1L);

		BankAccountDTO bankAccount = new BankAccountDTO();
		bankAccount.setBalance(new BigDecimal(1000));
		bankAccount.setTypeAccount("Caixa");
		bankAccount.setCreditCardClosingDate(Instant.parse("2026-05-29T00:00:00Z"));
		bankAccount.setUserId(user.getId());
		bankAccountService.insertBankAccount(bankAccount);
		BankAccount bankAccountDB = bankAccountService.findBankAccountById(1L);

		MonthlyExpenseDTO monthlyExpenseDTO = new MonthlyExpenseDTO();
		monthlyExpenseDTO.setMonthTotal(new BigDecimal(0));
		monthlyExpenseDTO.setMonth("Maio");
		monthlyExpenseDTO.setLimitExpense(new BigDecimal(500));
		monthlyExpenseDTO.setBankAccountId(bankAccountDB.getId());
		monthlyExpenseService.insertMonthlyExpense(monthlyExpenseDTO);
		MonthlyExpense monthlyExpense = monthlyExpenseService.findMonthlyExpenseById(1L);

		ExpenseDTO expenseDTO = new ExpenseDTO();
		expenseDTO.setAmount(new BigDecimal(100));
		expenseDTO.setDescription("Compra no supermercado");
		expenseDTO.setExpenseMoment(Instant.now());
		expenseDTO.setMonthlyExpenseId(monthlyExpense.getId());
		expenseDTO.setCategoryId(category.getId());
		expenseService.insertExpense(expenseDTO);
		Expense expense = expenseService.findExpenseById(1L);
		expenseService.processExpense(expense);

		ExpenseDTO expenseDTO1 = new ExpenseDTO();
		expenseDTO1.setAmount(new BigDecimal(150));
		expenseDTO1.setDescription("Compra no mercado");
		expenseDTO1.setExpenseMoment(Instant.now());
		expenseDTO1.setMonthlyExpenseId(monthlyExpense.getId());
		expenseDTO1.setCategoryId(category.getId());
		expenseService.insertExpense(expenseDTO1);
		Expense expense1 = expenseService.findExpenseById(2L);
		expenseService.processExpense(expense1);

		monthlyExpenseService.processMonthlyExpense(monthlyExpense);
	}

}
