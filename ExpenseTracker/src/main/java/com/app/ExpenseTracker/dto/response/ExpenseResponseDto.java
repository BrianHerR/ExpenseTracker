package com.app.ExpenseTracker.dto.response;

import com.app.ExpenseTracker.entity.Expense;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExpenseResponseDto {

    private Long id;
    private String categoryName;
    private double amount;
    private LocalDate date;
    private String description;


    public ExpenseResponseDto(Expense expense) {
        this.id = expense.getId();
        this.categoryName = expense.getCategory().getName();
        this.amount = expense.getAmount();
        this.date = expense.getDate();
        this.description = expense.getDescription();
    }
}
