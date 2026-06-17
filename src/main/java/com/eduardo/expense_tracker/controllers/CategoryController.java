package com.eduardo.expense_tracker.controllers;

import com.eduardo.expense_tracker.dtos.request.CategoryDTOrequest;
import com.eduardo.expense_tracker.dtos.response.CategoryDTOresponse;
import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/category")
@Tag(name = "Categories", description = "Operações relacionadas às Categorias")
public class CategoryController {

    private final CategoryService categoryServices;

    public CategoryController(CategoryService categoryServices) {
        this.categoryServices = categoryServices;
    }

    @GetMapping
    @Operation(summary = "Lista todas as categorias")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<List<CategoryDTOresponse>> findAll(){
        return ResponseEntity.ok().body(categoryServices.findAllCategories());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Listar categoria por Id")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    public ResponseEntity<CategoryDTOresponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(categoryServices.findCategoryById(id));
    }

    @PutMapping(value = "/update/{id}")
    @Operation(summary = "Atualiza uma categoria")
    @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    public ResponseEntity<CategoryDTOresponse> updateCategory(@PathVariable Long id, @RequestBody CategoryDTOrequest category) {
        return ResponseEntity.ok().body(categoryServices.updateCategory(id, category));
    }
    @PostMapping(value = "/insert")
    @Operation(summary = "Insere uma nova categoria")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados da categoria inválidos")
    public ResponseEntity<CategoryDTOresponse> insertCategory(@RequestBody CategoryDTOrequest category) {
        return ResponseEntity.ok().body(categoryServices.insertCategory(category));
    }
    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Exclui uma categoria")
    @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryServices.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
