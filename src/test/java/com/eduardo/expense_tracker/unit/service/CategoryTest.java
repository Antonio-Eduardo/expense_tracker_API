package com.eduardo.expense_tracker.unit.service;

import com.eduardo.expense_tracker.dtos.request.CategoryDTOrequest;
import com.eduardo.expense_tracker.dtos.response.CategoryDTOresponse;
import com.eduardo.expense_tracker.entities.Category;
import com.eduardo.expense_tracker.repositories.CategoryRepository;
import com.eduardo.expense_tracker.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    @Test
    public void deveriaCriarUmaCategoria(){
        CategoryDTOrequest categoryDTOrequest = new CategoryDTOrequest();
        categoryDTOrequest.setName("Alimentação");
        Category category = new Category();
        category.setName(categoryDTOrequest.getName());

        when(repository.save(any(Category.class))).thenReturn(category);

        CategoryDTOresponse savedCategory = service.insertCategory(categoryDTOrequest);

        assertNotNull(savedCategory);
        assertEquals("Alimentação", savedCategory.getName());

        verify(repository).save(any(Category.class));

    }
    @Test
    public void deveriaAcharUmaCategoriaPorId(){
        Category category = new Category();
        category.setId(1L);
        category.setExpenses(new ArrayList<>());

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));

        CategoryDTOresponse categoryFind = service.findCategoryById(category.getId());

        assertNotNull(categoryFind);
        assertEquals(1L,categoryFind.getId());

        verify(repository).findById(any(Long.class));
    }

    @Test
    public void deveriaAcharTodasAsCategorias(){
        List<Category> lista = List.of(new Category(), new Category());

        when(repository.findAll()).thenReturn(lista);

        List<CategoryDTOresponse> response = service.findAllCategories();

        assertNotNull(response);
        assertEquals(2,response.size());

        verify(repository).findAll();
    }

    @Test
    public void deveriaDeleterUmaCategoriaPorId(){
        service.deleteCategory(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    public void deveriaAtualizarUmaCategoriaPeloId(){
        Category category = new Category();
        category.setId(1L);
        category.setName("Alimento");

        CategoryDTOrequest categoryDTOrequest = new CategoryDTOrequest();
        categoryDTOrequest.setName("Novo");

        when(repository.save(any(Category.class))).thenReturn(category);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));

        CategoryDTOresponse categoryDTOresponse = service.updateCategory(category.getId(), categoryDTOrequest);

        assertNotNull(categoryDTOresponse);
        assertEquals("Novo", categoryDTOresponse.getName());

        verify(repository).save(any(Category.class));
    }
}
