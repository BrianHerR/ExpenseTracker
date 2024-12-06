package com.app.ExpenseTracker.dto.request;

import com.app.ExpenseTracker.entity.Category;
import com.app.ExpenseTracker.entity.Expense;
import com.app.ExpenseTracker.entity.User;
import com.app.ExpenseTracker.exception.UserNotFoundException;

import com.app.ExpenseTracker.service.UserService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter

public class ExpenseRequestDto {

    private final UserService userService;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long userId;

    @NotNull(message = "El nombre de la categoria no puede ser nulo")
    @Size(max = 40, message = "El nombre de la categoria no puede tener mas de 40 caracteres")
    private String categoryName;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate date;

    @Positive(message = "El monto debe ser un valor positivo")
    private double amount;

    @Size(max = 250, message = "La descripción no puede exceder los 250 caracteres")
    private String description;




    public Expense toEntity() throws UserNotFoundException {
        User user = userService.findUserById(userId);
        Category category = new Category();
        category.setName(categoryName);

        return new Expense(null, user, category, amount, date, description);
    }
}