package com.app.ExpenseTracker.service.impl;

import com.app.ExpenseTracker.dto.response.CategoryResponseDto;
import com.app.ExpenseTracker.entity.Category;
import com.app.ExpenseTracker.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<Category> mockCategories = List.of(
                new Category( "comida"),
                new Category("transporte"),
                new Category("cine")
                );

        when(categoryRepository.findAll()).thenReturn(mockCategories);

        List<CategoryResponseDto> response = categoryService.findAll();

        assertNotNull(response);
        assertEquals(3, response.size());
        assertEquals("comida", response.get(0).getName());

    }

    @Test
    void saveCategory() {

        String categoryName = "comida";
        Category mockCategory = new Category();
        mockCategory.setName(categoryName);

        when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

        CategoryResponseDto response = categoryService.saveCategory(categoryName);

        assertNotNull(response);
        assertEquals(categoryName, response.getName());
    }
}