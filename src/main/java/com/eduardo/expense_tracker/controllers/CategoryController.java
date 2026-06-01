package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.CategoryDTOrequest;
import com.eduardo.expense_tracker.dtos.response.CategoryDTOresponse;
import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryServices;

    @GetMapping
    public ResponseEntity<List<CategoryDTOresponse>> findAll(){
        return ResponseEntity.ok().body(categoryServices.findAllCategories());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTOresponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(categoryServices.findCategoryById(id));
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return ResponseEntity.ok().body(categoryServices.updateCategory(id, category));
    }
    @PostMapping(value = "/insert")
    public ResponseEntity<CategoryDTOresponse> insertCategory(@RequestBody CategoryDTOrequest category) {
        return ResponseEntity.ok().body(categoryServices.insertCategory(category));
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryServices.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
