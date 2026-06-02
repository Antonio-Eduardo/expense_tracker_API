package com.eduardo.expense_tracker.integration;

import com.eduardo.expense_tracker.TestcontainersConfiguration;
import com.eduardo.expense_tracker.dtos.request.ExpenseDTOrequest;
import com.eduardo.expense_tracker.dtos.response.ExpenseDTOresponse;
import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ExpenseIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private MonthlyExpenseRepository monthlyExpenseRepository;

	@Autowired
	private BankAccountRepository bankAccountRepository;

	private Category category;
	private MonthlyExpense monthlyExpense;
	private BankAccount bankAccount;

	@BeforeEach
	void setup() {
		// Limpar dados antigos
		expenseRepository.deleteAll();
		monthlyExpenseRepository.deleteAll();
		categoryRepository.deleteAll();
		bankAccountRepository.deleteAll();

		// Setup: Criar BankAccount
		bankAccount = new BankAccount();
		bankAccount.setName("Minha Conta");
		bankAccount.setBalance(new BigDecimal("5000.00"));
		bankAccountRepository.save(bankAccount);

		// Setup: Criar Category
		category = new Category();
		category.setName("Transporte");
		category.setDescription("Despesas com transporte");
		categoryRepository.save(category);

		// Setup: Criar MonthlyExpense
		monthlyExpense = new MonthlyExpense();
		monthlyExpense.setMonth("junho");
		monthlyExpense.setMonthTotal(BigDecimal.ZERO);
		monthlyExpense.setLimitExpense(new BigDecimal("2000.00"));
		monthlyExpense.setBankAccount(bankAccount);
		monthlyExpenseRepository.save(monthlyExpense);
	}

	@Test
	void shouldCreateExpenseSuccessfully() {
		// Arrange
		ExpenseDTOrequest expenseRequest = new ExpenseDTOrequest();
		expenseRequest.setDescription("Uber para o trabalho");
		expenseRequest.setAmount(new BigDecimal("35.50"));
		expenseRequest.setMonthlyExpenseId(monthlyExpense.getId());
		expenseRequest.setCategoryId(category.getId());

		// Act
		ResponseEntity<ExpenseDTOresponse> response = restTemplate.postForEntity(
				"/expense/insert",
				expenseRequest,
				ExpenseDTOresponse.class
		);

		// Assert
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getId());
		assertEquals("Uber para o trabalho", response.getBody().getDescription());
		assertEquals(BigDecimal.valueOf(35.50), response.getBody().getAmount());
		assertEquals(monthlyExpense.getId(), response.getBody().getMonthlyExpenseId());
		assertEquals(category.getId(), response.getBody().getCategoryId());

		// Verificar que foi salvo no banco (banco real via Testcontainers)
		assertEquals(1, expenseRepository.count());
	}

	@Test
	void shouldFindExpenseById() {
		// Arrange: Criar uma despesa via repository (banco real)
		var expense = new com.eduardo.expense_tracker.entities.Expense();
		expense.setDescription("Almoço");
		expense.setAmount(new BigDecimal("45.00"));
		expense.setMonthlyExpense(monthlyExpense);
		expense.setCategory(category);
		var savedExpense = expenseRepository.save(expense);

		// Act
		ResponseEntity<ExpenseDTOresponse> response = restTemplate.getForEntity(
				"/expense/{id}",
				ExpenseDTOresponse.class,
				savedExpense.getId()
		);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(savedExpense.getId(), response.getBody().getId());
		assertEquals("Almoço", response.getBody().getDescription());
		assertEquals(BigDecimal.valueOf(45.00), response.getBody().getAmount());
	}

	@Test
	void shouldFindAllExpenses() {
		// Arrange: Criar múltiplas despesas via repository
		var expense1 = new com.eduardo.expense_tracker.entities.Expense();
		expense1.setDescription("Uber");
		expense1.setAmount(new BigDecimal("25.00"));
		expense1.setMonthlyExpense(monthlyExpense);
		expense1.setCategory(category);
		expenseRepository.save(expense1);

		var expense2 = new com.eduardo.expense_tracker.entities.Expense();
		expense2.setDescription("Táxi");
		expense2.setAmount(new BigDecimal("30.00"));
		expense2.setMonthlyExpense(monthlyExpense);
		expense2.setCategory(category);
		expenseRepository.save(expense2);

		// Act
		ResponseEntity<ExpenseDTOresponse[]> response = restTemplate.getForEntity(
				"/expense",
				ExpenseDTOresponse[].class
		);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(2, response.getBody().length);
		assertEquals("Uber", response.getBody()[0].getDescription());
		assertEquals("Táxi", response.getBody()[1].getDescription());
	}

	@Test
	void shouldDeleteExpense() {
		// Arrange
		var expense = new com.eduardo.expense_tracker.entities.Expense();
		expense.setDescription("Gasto a deletar");
		expense.setAmount(new BigDecimal("50.00"));
		expense.setMonthlyExpense(monthlyExpense);
		expense.setCategory(category);
		var savedExpense = expenseRepository.save(expense);

		assertEquals(1, expenseRepository.count());

		// Act
		restTemplate.delete("/expense/delete/{id}", savedExpense.getId());

		// Assert
		assertEquals(0, expenseRepository.count());
	}

	@Test
	void shouldReturnNotFoundWhenCreatingExpenseWithInvalidMonthlyExpense() {
		// Arrange
		ExpenseDTOrequest expenseRequest = new ExpenseDTOrequest();
		expenseRequest.setDescription("Uber");
		expenseRequest.setAmount(new BigDecimal("25.00"));
		expenseRequest.setMonthlyExpenseId(99999L); // ID que não existe
		expenseRequest.setCategoryId(category.getId());

		// Act
		ResponseEntity<ExpenseDTOresponse> response = restTemplate.postForEntity(
				"/expense/insert",
				expenseRequest,
				ExpenseDTOresponse.class
		);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void shouldReturnNotFoundWhenFindingNonExistentExpense() {
		// Act
		ResponseEntity<ExpenseDTOresponse> response = restTemplate.getForEntity(
				"/expense/{id}",
				ExpenseDTOresponse.class,
				99999L
		);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}