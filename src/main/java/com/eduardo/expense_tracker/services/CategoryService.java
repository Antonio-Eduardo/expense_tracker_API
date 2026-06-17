package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.dtos.request.CategoryDTOrequest;
import com.eduardo.expense_tracker.dtos.request.ExpenseDTOrequest;
import com.eduardo.expense_tracker.dtos.response.CategoryDTOresponse;
import com.eduardo.expense_tracker.dtos.response.ExpenseDTOresponse;
import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.entities.Expense;
import com.eduardo.expense_tracker.repositories.CategoryRepository;
import com.eduardo.expense_tracker.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {


        private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
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

        @Transactional
        public void deleteCategory(Long id) {
            repository.deleteById(id);
        }

        public void updateData(Category categoryFind, CategoryDTOrequest obj){
            if (obj.getNotifyLimit() != null) {
                categoryFind.setNotifyLimit(obj.getNotifyLimit());
            }
            if (obj.getName() != null) {
                categoryFind.setName(obj.getName());
            }
        }

        @Transactional
        public CategoryDTOresponse updateCategory(Long id, CategoryDTOrequest obj){
            Category categoryFind = repository.findById(id).orElseThrow(
                    ()  -> new ResourceNotFoundException("Category not found" + id));
                updateData(categoryFind, obj);
                categoryFind = repository.save(categoryFind);
               return convertToResponseDTO(categoryFind);
        }
    public CategoryDTOresponse convertToResponseDTO(Category category) {

        CategoryDTOresponse response = new CategoryDTOresponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setNotifyLimit(category.getNotifyLimit());
        if (category.getExpenses() != null){
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
    } else {
            response.setExpenseDTOS(null);
        }
        return response;
    }
}
