package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.request.CategoryDTO;
import com.eduardo.expense_tracker.dtos.request.ExpenseDTO;
import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.repositories.CategoryRepository;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
        private CategoryRepository repository;

        public CategoryDTO insertCategory(CategoryDTO categoryDTO){
            Category category = new Category();
            category.setName(categoryDTO.getName());
            category.setNotifyLimit(categoryDTO.getNotifyLimit());

            Category saveCategory = repository.save(category);

            CategoryDTO response = new CategoryDTO();
            response.setName(saveCategory.getName());
            response.setNotifyLimit(saveCategory.getNotifyLimit());

            return response;
        }

        public CategoryDTO findCategoryById(Long id){
            Category category = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not fond" + id));
            CategoryDTO categoryDTO =  new CategoryDTO();
            categoryDTO.setName(category.getName());
            categoryDTO.setNotifyLimit(category.getNotifyLimit());
            if (category.getExpenses() != null){
                List<ExpenseDTO> expenseDTOList = category.getExpenses().stream().map(
                        expense -> new ExpenseDTO(
                                expense.getAmount(),
                                expense.getDescription(),
                                expense.getExpenseMoment(),
                                expense.getCategory().getId(),
                                expense.getMonthlyExpense().getId()
                        )).toList();
                categoryDTO.setExpenseDTOS(expenseDTOList);
            }
           return categoryDTO;
        }

        public List<Category> findAllCategories(){
            return repository.findAll();
        }

        public void deleteCategory(Long id) {
            repository.deleteById(id);
        }

        public void updateData(Category categoryFind, Category obj){
            categoryFind.setNotifyLimit(obj.getNotifyLimit());
            categoryFind.setName(obj.getName());
        }

        public Category updateCategory(Long id, Category obj){
            Category categoryFind = repository.findById(id).orElse(null);
            if (categoryFind != null) {
                updateData(categoryFind, obj);
               return repository.save(categoryFind);
            }
            return null;
        }
}
