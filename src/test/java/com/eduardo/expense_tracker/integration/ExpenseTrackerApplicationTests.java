package com.eduardo.expense_tracker.integration;

import com.eduardo.expense_tracker.TestcontainersConfiguration;
import com.eduardo.expense_tracker.dtos.request.ExpenseDTOrequest;
import com.eduardo.expense_tracker.entities.BankAccount;
import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.entities.Expense;
import com.eduardo.expense_tracker.entities.MonthlyExpense;
import com.eduardo.expense_tracker.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
class ExpenseIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

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
		expenseRepository.deleteAll();
		monthlyExpenseRepository.deleteAll();
		categoryRepository.deleteAll();
		bankAccountRepository.deleteAll();

		bankAccount = new BankAccount();
		bankAccount.setTypeAccount("Conta Principal");
		bankAccount.setBalance(new BigDecimal("5000.00"));
		bankAccount = bankAccountRepository.save(bankAccount);

		category = new Category();
		category.setName("Transporte");
		category = categoryRepository.save(category);

		monthlyExpense = new MonthlyExpense();
		monthlyExpense.setMonth("Junho");
		monthlyExpense.setMonthTotal(BigDecimal.ZERO);
		monthlyExpense.setLimitExpense(new BigDecimal("2000.00"));
		monthlyExpense.setBankAccount(bankAccount);
		monthlyExpense = monthlyExpenseRepository.save(monthlyExpense);
	}

	@Test
	void deveriaCriarUmaExpense() throws Exception {
		ExpenseDTOrequest request = new ExpenseDTOrequest();
		request.setDescription("Uber");
		request.setAmount(new BigDecimal("35.50"));
		request.setMonthlyExpenseId(monthlyExpense.getId());
		request.setCategoryId(category.getId());

		mockMvc.perform(post("/expense/insert")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                    {"description":"Uber","amount":35.50,"monthlyExpenseId":%d,"categoryId":%d}
                    """.formatted(monthlyExpense.getId(), category.getId())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.description").value("Uber"))
				.andExpect(jsonPath("$.amount").value(35.50))
				.andExpect(jsonPath("$.monthlyExpenseId").value(monthlyExpense.getId()))
				.andExpect(jsonPath("$.categoryId").value(category.getId()));
	}

	@Test
	void DeveriaRetornarUmaExpensePeloId() throws Exception {
		var expense = new com.eduardo.expense_tracker.entities.Expense();
		expense.setDescription("Almoço");
		expense.setAmount(new BigDecimal("45.00"));
		expense.setMonthlyExpense(monthlyExpense);
		expense.setCategory(category);
		expense = expenseRepository.save(expense);

		mockMvc.perform(get("/expense/{id}", expense.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.description").value("Almoço"))
				.andExpect(jsonPath("$.amount").value(45.00));
	}

	@Test
	void DeveriaRetornarTodasAsExpenses() throws Exception {
		var expense1 = new Expense();
		expense1.setDescription("Uber");
		expense1.setAmount(new BigDecimal("20.00"));
		expense1.setMonthlyExpense(monthlyExpense);
		expense1.setCategory(category);
		expenseRepository.save(expense1);

		var expense2 = new Expense();
		expense2.setDescription("Táxi");
		expense2.setAmount(new BigDecimal("40.00"));
		expense2.setMonthlyExpense(monthlyExpense);
		expense2.setCategory(category);
		expenseRepository.save(expense2);

		mockMvc.perform(get("/expense"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	void deveriaDeleterUmaExpense() throws Exception {
		var expense = new Expense();
		expense.setDescription("Excluir");
		expense.setAmount(new BigDecimal("15.00"));
		expense.setMonthlyExpense(monthlyExpense);
		expense.setCategory(category);
		expense = expenseRepository.save(expense);

		mockMvc.perform(delete("/expense/delete/{id}", expense.getId()))
				.andExpect(status().isNoContent());
	}

	@Test
	void DeveriaRetornarNotFoundQuandoExpenseNaoExister() throws Exception {
		mockMvc.perform(get("/expense/{id}", 9999L))
				.andExpect(status().isNotFound());
	}
}