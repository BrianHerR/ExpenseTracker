package com.app.ExpenseTracker.controller;

import com.app.ExpenseTracker.dto.response.CategoryResponseDto;
import com.app.ExpenseTracker.service.CategoryService;
import com.app.ExpenseTracker.service.ExpenseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/categorys")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    public ResponseEntity<?> getAllCategory() {

        try {
            log.info("Solicitud recibida para obtener todas las categorías.");
            List<CategoryResponseDto> categories = categoryService.findAll();
            log.info("Se recuperaron {} categorías exitosamente.", categories.size());
            return ResponseEntity.ok(categories);

        } catch (Exception e) {
            log.error("Error al obtener las categorías: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al obtener las categorías: " + e.getMessage());

        }
    }
}
