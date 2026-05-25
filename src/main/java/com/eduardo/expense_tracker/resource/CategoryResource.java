package com.eduardo.expense_tracker.resource;

import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryResource {

    @Autowired
    private CategoryServices categoryServices;

    @GetMapping
    public ResponseEntity<List<Category>> findAll(){
        return ResponseEntity.ok().body(categoryServices.findAllCategories());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(categoryServices.findCategoryById(id));
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return ResponseEntity.ok().body(categoryServices.updateCategory(id, category));
    }
    @PostMapping(value = "/insert")
    public ResponseEntity<Category> insertCategory(@RequestBody Category category) {
        return ResponseEntity.ok().body(categoryServices.insertCategory(category));
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryServices.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
