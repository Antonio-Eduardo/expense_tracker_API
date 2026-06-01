package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.request.CategoryDTOrequest;
import com.eduardo.expense_tracker.dtos.request.ExpenseDTOrequest;
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

        public CategoryDTOrequest insertCategory(CategoryDTOrequest categoryDTO){
            Category category = new Category();
            category.setName(categoryDTO.getName());
            category.setNotifyLimit(categoryDTO.getNotifyLimit());

            Category saveCategory = repository.save(category);

            CategoryDTOrequest response = new CategoryDTOrequest();
            response.setName(saveCategory.getName());
            response.setNotifyLimit(saveCategory.getNotifyLimit());

            return response;
        }

        public CategoryDTOrequest findCategoryById(Long id){
            Category category = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not fond" + id));
            CategoryDTOrequest categoryDTO =  new CategoryDTOrequest();
            categoryDTO.setName(category.getName());
            categoryDTO.setNotifyLimit(category.getNotifyLimit());
            if (category.getExpenses() != null){
                List<ExpenseDTOrequest> expenseDTOList = category.getExpenses().stream().map(
                        expense -> new ExpenseDTOrequest(
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
