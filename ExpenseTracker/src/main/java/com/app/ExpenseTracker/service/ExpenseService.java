package com.app.ExpenseTracker.service;

import com.app.ExpenseTracker.dto.request.ExpenseRequestDto;
import com.app.ExpenseTracker.dto.response.ExpenseResponseDto;
import com.app.ExpenseTracker.entity.Expense;

import java.util.List;
import java.util.Map;

public interface ExpenseService {


    List<ExpenseResponseDto> findExpensesByuser(Long userId);

    ExpenseResponseDto findExpenseById(Long id);

    Double totalExpenses(Long userId, int month);

    Map<String, Double> percentage(Long userId, int month);

    ExpenseResponseDto addExpense(Long userId, ExpenseRequestDto expenseRequestDto);

    ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto expenseRequestDto);

    void deleteExpense(Long id);

}
