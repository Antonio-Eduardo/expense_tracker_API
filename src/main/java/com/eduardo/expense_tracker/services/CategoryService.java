package com.eduardo.expense_tracker.services;

import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
        private CategoryRepository repository;

        public Category insertCategory(Category category){

            return repository.save(category);
        }

        public Category findCategoryById(Long id){
           return repository.findById(id).orElse(null);
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
