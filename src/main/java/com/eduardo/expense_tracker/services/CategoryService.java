package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.request.CategoryDTOrequest;
import com.eduardo.expense_tracker.dtos.request.ExpenseDTOrequest;
import com.eduardo.expense_tracker.dtos.response.CategoryDTOresponse;
import com.eduardo.expense_tracker.dtos.response.ExpenseDTOresponse;
import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.entities.Expense;
import com.eduardo.expense_tracker.repositories.CategoryRepository;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
        private CategoryRepository repository;

        public CategoryDTOresponse insertCategory(CategoryDTOrequest categoryDTO){
            Category category = new Category();
            category.setName(categoryDTO.getName());
            category.setNotifyLimit(categoryDTO.getNotifyLimit());

            Category saveCategory = repository.save(category);

            CategoryDTOresponse response = new CategoryDTOresponse();
            response.setId(saveCategory.getId());
            response.setName(saveCategory.getName());
            response.setNotifyLimit(saveCategory.getNotifyLimit());

            return response;
        }

        public CategoryDTOresponse findCategoryById(Long id){
            Category category = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not fond" + id));

           return convertToResponseDTO(category);
        }

        public List<CategoryDTOresponse> findAllCategories(){

            return repository.findAll().stream().map(
                    this::convertToResponseDTO).toList();
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
    public CategoryDTOresponse convertToResponseDTO(Category category){

        CategoryDTOresponse response = new CategoryDTOresponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setNotifyLimit(category.getNotifyLimit());
        Set<ExpenseDTOresponse> expenseDTOList = category.getExpenses().stream().map(
                expense -> new ExpenseDTOresponse(
                        expense.getId(),
                        expense.getAmount(),
                        expense.getDescription(),
                        expense.getExpenseMoment(),
                        expense.getCategory().getId(),
                        expense.getMonthlyExpense().getId()
                )).collect(Collectors.toSet());
        response.setExpenseDTOS(expenseDTOList);

        return response;
    }
}
