package com.app.ExpenseTracker.service.impl;

import com.app.ExpenseTracker.dto.request.ExpenseRequestDto;
import com.app.ExpenseTracker.dto.response.ExpenseResponseDto;
import com.app.ExpenseTracker.entity.Category;
import com.app.ExpenseTracker.entity.Expense;
import com.app.ExpenseTracker.entity.User;
import com.app.ExpenseTracker.exception.UserNotFoundException;
import com.app.ExpenseTracker.repository.ExpenseRepository;
import com.app.ExpenseTracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Long userId;
    private User user;
    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    void setUp(){

        MockitoAnnotations.openMocks(this);

        userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setName("Brian");
        user.setEmail("brian@hotmail.com");

        Category category1 = new Category();
        category1.setName("Comida");

        Category category2 = new Category();
        category2.setName("Transporte");

        expense1 = new Expense(1L, user, category1, 100.0, LocalDate.now(), "Cena en restaurante");
        expense2 = new Expense(2L, user, category2, 50.0, LocalDate.now(), "Taxi");
    }

    @Test
    void findExpensesByuser() {

        when(expenseRepository.findByUserId(userId)).thenReturn(List.of(expense1, expense2));

        List<ExpenseResponseDto> expenses = expenseService.findExpensesByuser(userId);

        assertNotNull(expenses);
        assertEquals(2, expenses.size());
        assertEquals("Comida", expenses.get(0).getCategoryName());
        assertEquals("Transporte", expenses.get(1).getCategoryName());
        assertEquals(100.0, expenses.get(0).getAmount());
        assertEquals(50.0, expenses.get(1).getAmount());
    }

    @Test
    void findExpenseById() {

        Long expenseId = 1L;
        when(expenseRepository.findById(expenseId)).thenReturn(java.util.Optional.of(expense1));

        ExpenseResponseDto result = expenseService.findExpenseById(expenseId);

        assertNotNull(result);
        assertEquals("Comida", result.getCategoryName());
        assertEquals(100.0, result.getAmount());

    }

    @Test
    void totalExpenses() {

        int month = 12;
        List<Expense> expenses = List.of(
                new Expense(1L, user, new Category("Comida"), 100.0, LocalDate.of(2023, 12, 10), "Cena en restaurante"),
                new Expense(2L, user, new Category("Transporte"), 50.0, LocalDate.of(2023, 12, 15), "Taxi")
        );

        when(expenseRepository.findByUserIdAndDateMonth(userId, month)).thenReturn(expenses);

        Double total = expenseService.totalExpenses(userId, month);

        assertNotNull(total);
        assertEquals(150.0, total);

    }

    @Test
    void percentage() {
        int month = 12;
        Double total = 150.0;
        List<Expense> expenses = List.of(
                new Expense(1L, user, new Category("Comida"), 100.0, LocalDate.of(2023, 12, 10), "Cena en restaurante"),
                new Expense(2L, user, new Category("Transporte"), 50.0, LocalDate.of(2023, 12, 15), "Taxi")
        );

        when(expenseRepository.findByUserIdAndDateMonth(userId, month)).thenReturn(expenses);
        when(expenseRepository.findByUserId(userId)).thenReturn(expenses);

        Map<String, Double> percentages = expenseService.percentage(userId, month);

        assertNotNull(percentages);
        assertEquals(2, percentages.size());
        assertEquals(66.67, percentages.get("Comida"), 0.01);
        assertEquals(33.33, percentages.get("Transporte"),0.01);
    }

    @Test
    void addExpense() throws UserNotFoundException {

        ExpenseRequestDto expenseRequest = new ExpenseRequestDto(
                userService, userId, "Comida", LocalDate.now(), 200.0, "Almuerzo");

        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userService.findUserById(userId)).thenReturn(user);

        ExpenseResponseDto expense = expenseService.addExpense(userId, expenseRequest);

        assertNotNull(expense);
        assertEquals("Comida", expense.getCategoryName());
        assertEquals(200.0, expense.getAmount());
    }

    @Test
    void updateExpense() {

        Long expenseId = 1L;


        Expense existingExpense = new Expense(expenseId, user, new Category("Comida"), 100.0, LocalDate.of(2023, 12, 10), "Cena en restaurante");


        ExpenseRequestDto expenseRequestDto = new ExpenseRequestDto(
                userService, userId, "Transporte", LocalDate.of(2023, 12, 15), 50.0, "Taxi");


        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingExpense));


        when(expenseRepository.save(existingExpense)).thenReturn(existingExpense);


        ExpenseResponseDto updatedExpense = expenseService.updateExpense(expenseId, expenseRequestDto);


        assertNotNull(updatedExpense);
        assertEquals("Transporte", updatedExpense.getCategoryName());
        assertEquals(50.0, updatedExpense.getAmount());
        assertEquals("Taxi", updatedExpense.getDescription());
        assertEquals(LocalDate.of(2023, 12, 15), updatedExpense.getDate());

    }

    @Test
    void deleteExpense() {

        Long expenseId = 1L;

        when(expenseRepository.existsById(expenseId)).thenReturn(true);

        expenseService.deleteExpense(expenseId);

        verify(expenseRepository, times(1)).existsById(expenseId);
        verify(expenseRepository, times(1)).deleteById(expenseId);


    }
}