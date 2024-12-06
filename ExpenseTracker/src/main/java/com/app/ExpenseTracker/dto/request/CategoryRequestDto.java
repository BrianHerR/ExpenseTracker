package com.app.ExpenseTracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class CategoryRequestDto {

    @NotBlank(message = "El nombre de la categoria no puede estar vacio.")
    @Size(max = 40, message = "El nombre de la categoria no puede tener mas de 40 caracteres")
    private String name;

}
