package com.app.ExpenseTracker.controller;

import com.app.ExpenseTracker.dto.response.CategoryResponseDto;
import com.app.ExpenseTracker.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void getAllCategory_ReturnListOfCategory() throws Exception {

        List<CategoryResponseDto> mockCategories = List.of(
                new CategoryResponseDto("comida"),
                new CategoryResponseDto("transporte"),
                new CategoryResponseDto("cine")
        );

        //given
        given(categoryService.findAll()).willReturn(mockCategories);

        // when + then
        mockMvc.perform(get("/api/v1/categorys").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].name").value("comida"));

    }
}