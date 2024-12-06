package com.app.ExpenseTracker.controller;

import com.app.ExpenseTracker.dto.request.ExpenseRequestDto;
import com.app.ExpenseTracker.dto.response.ExpenseResponseDto;
import com.app.ExpenseTracker.entity.Expense;
import com.app.ExpenseTracker.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/expenses")
@AllArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getExpensesByUser(@PathVariable Long userId) {

        try {

            log.info("Solicitud recibida para obtener gastos del usuario de ID: {}", userId);
            List<ExpenseResponseDto> expenses = expenseService.findExpensesByuser(userId);
            log.info("Lista de Gastos del usuario de ID: {} recuperado exitosamente", userId);
            return ResponseEntity.ok(expenses);

        } catch (Exception e) {
            log.error("Error al obtener los gastos del usuario con id {}: {}", userId, e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener los gastos del usuario: " + e.getMessage());

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long id) {

        try {
            log.info("Solicitud recibida para obtener gasto con ID: {}", id);
            ExpenseResponseDto findExpense = expenseService.findExpenseById(id);
            log.info("Gasto con ID: {} recuperado exitosamente", id);
            return ResponseEntity.ok(findExpense);

        } catch (Exception e) {
            log.error("Error al obtener el gasto con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener el gasto: " + e.getMessage());

        }
    }

    @GetMapping("/{userId}/total-expenses")
    public ResponseEntity<?> getMonthlyTotalExpenses(@PathVariable Long userId,
                                                     @RequestParam("month") int month) {
        try {

            log.info("Solicitud recibida para calcular el total de gastos del usuario ID: {} para el mes: {}", userId, month);
            Double totalExpenses = expenseService.totalExpenses(userId, month);
            log.info("Total de gastos para el usuario ID: {} en el mes {} calculado exitosamente: {}", userId, month, totalExpenses);
            return ResponseEntity.ok(totalExpenses);

        } catch (Exception e) {
            log.error("Error al calcular el total de gastos del usuario ID {} en el mes {}: {}", userId, month, e.getMessage());
            return ResponseEntity.status(500).body("Error al calcular el total de gastos: " + e.getMessage());

        }
    }

    @GetMapping("/{userId}/category-percentage")
    public ResponseEntity<?> getCategoryPercentage(@PathVariable Long userId,
                                                   @RequestParam("month") int month) {
        try {
            log.info("Solicitud recibida para calcular el porcentaje por categoria del usuario ID: {} en el mes: {}", userId, month);
            Map<String, Double> categoryPercentage = expenseService.percentage(userId, month);
            log.info("Porcentaje por categoria para el usuario ID: {} en el mes {} calculado exitosamente", userId, month);
            return ResponseEntity.ok(categoryPercentage);

        } catch (Exception e) {
            log.error("Error al calcular el porcentaje por categoria del usuario ID {} en el mes {}: {}", userId, month, e.getMessage());
            return ResponseEntity.status(500).body("Error al calcular el porcentaje por categoría: " + e.getMessage());

        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> createExpense(@PathVariable Long userId, @RequestBody @Valid ExpenseRequestDto expenseRequestDto) {

        try {
            log.info("Solicitud recibida para crear un nuevo gasto para el usuario ID: {}", userId);
            ExpenseResponseDto createdExpense = expenseService.addExpense(userId, expenseRequestDto);
            log.info("Gasto creado exitosamente para el usuario ID: {} con ID de gasto: {}", userId, createdExpense.getId());
            return ResponseEntity.ok(createdExpense);

        } catch (Exception e) {
            log.error("Error al crear el gasto para el usuario ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(500).body("Error al crear el gasto: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody @Valid ExpenseRequestDto expenseRequestDto) {

        try {
            log.info("Solicitud recibida para actualizar el gasto con ID: {}", id);
            ExpenseResponseDto updatedExpense = expenseService.updateExpense(id, expenseRequestDto);
            log.info("Gasto con ID: {} actualizado exitosamente", id);
            return ResponseEntity.ok(updatedExpense);

        } catch (Exception e) {
            log.error("Error al actualizar el gasto con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body("Error al actualizar el gasto: " + e.getMessage());

        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        try {
            log.info("Solicitud recibida para eliminar el gasto con ID: {}", id);
            expenseService.deleteExpense(id);
            log.info("Gasto con ID: {} eliminado exitosamente", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar el gasto con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body("Error al eliminar el gasto: " + e.getMessage());
        }
    }
}


















