package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServices {

    @Autowired
        CategoryServices repository;

        public Category insertCategory(CategoryServices category){
            return repository.insertCategory(category);
        }
        public Category findCategoryById(Long id){
           return repository.findCategoryById(id);
        }
        public Category findAllCategories(){
            return repository.findAllCategories();
        }
        public void deleteCategory(Long id) {
            repository.deleteCategory(id);
        }
        public void updateData(Category categoryFind, Category obj){
            categoryFind.setNotifyLimit(obj.getNotifyLimit());
            categoryFind.setName(obj.getName());
        }
        public Category updateCategory(Long id, Category obj){
            Category categoryFind = repository.findCategoryById(id);
            if (categoryFind != null) {
                updateData(categoryFind, obj);
               return repository.updateCategory(id, categoryFind);
            }
            return null;
        }
}
