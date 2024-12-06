package com.app.ExpenseTracker.service.impl;

import com.app.ExpenseTracker.dto.response.CategoryResponseDto;
import com.app.ExpenseTracker.entity.Category;
import com.app.ExpenseTracker.entity.Expense;
import com.app.ExpenseTracker.exception.ExpenseNotFoundException;
import com.app.ExpenseTracker.repository.CategoryRepository;
import com.app.ExpenseTracker.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor

public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;


    @Override
    public List<CategoryResponseDto> findAll() {
        log.info("Inicio del método findAll en CategoryServiceImpl.");
        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty()){
            log.warn("La lista de categorías está vacía. Lanzando ExpenseNotFoundException.");
            throw new ExpenseNotFoundException("La lista de categorias se encuentra vacia.");
        }
        List<CategoryResponseDto> responseDto = categories.stream()
                        .map(category -> new CategoryResponseDto(category.getName()))
                        .distinct().toList();

        log.info("Se encontraron {} categorías en la base de datos.", categories.size());

        return responseDto;
    }

    public CategoryResponseDto saveCategory(String categoryName){
        log.info("Guardando categoria nueva: {}",categoryName);

        if (categoryName.trim().isEmpty()){
           log.warn("El nombre de la categoria esta vacio.");
           throw new IllegalArgumentException("El nombre de la categoria no puede estar vacio.");
        }

        Category category = new Category();
        category.setName(categoryName);

        Category savedCategory = categoryRepository.save(category);
        log.info("Categoria guardada: {}", savedCategory);

        return new CategoryResponseDto(savedCategory.getName());
    }
}
