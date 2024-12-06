package com.app.ExpenseTracker.service;



import com.app.ExpenseTracker.dto.response.CategoryResponseDto;

import java.util.List;


public interface CategoryService {
    List<CategoryResponseDto> findAll();

    CategoryResponseDto saveCategory(String categoryName);
}
