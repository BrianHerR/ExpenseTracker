package com.app.ExpenseTracker.service.impl;
import com.app.ExpenseTracker.dto.request.ExpenseRequestDto;
import com.app.ExpenseTracker.dto.response.ExpenseResponseDto;
import com.app.ExpenseTracker.entity.Category;
import com.app.ExpenseTracker.entity.Expense;
import com.app.ExpenseTracker.exception.ExpenseNotFoundException;
import com.app.ExpenseTracker.exception.InvalidExpenseDataException;
import com.app.ExpenseTracker.repository.ExpenseRepository;
import com.app.ExpenseTracker.service.ExpenseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    ExpenseRepository expenseRepository;

    @Override
    public List<ExpenseResponseDto> findExpensesByuser(Long userId) {

        log.info("Buscando gastos para el usuario con ID: {}", userId);
        if(userId == null || userId <= 0){
            log.warn("El ID de usuario proporcionado es inválido: {}", userId);
            throw new InvalidExpenseDataException("El ID de usuario es invalido "+ userId);
        }

        List<ExpenseResponseDto> expenses = expenseRepository.findByUserId(userId)
                .stream()
                .map(ExpenseResponseDto::new)
                .toList();

        if(expenses.isEmpty()){
            log.warn("No se encontraron gastos para el usuario con ID: {}", userId);
            throw new ExpenseNotFoundException("No se encontraron gastos para el usuario con ID: " + userId);
        }

        log.info("Se encontraron {} gastos para el usuario con ID: {}", expenses.size(), userId);
        return expenses;
    }

    @Override
    public ExpenseResponseDto findExpenseById(Long id) {
        log.info("Buscando gasto con ID: {}", id);

        if (id == null || id <= 0){
            log.warn("El ID del gasto proporcionado es invalido: {}", id);
            throw new InvalidExpenseDataException("El ID del gasto es invalido "+ id);
        }


        return expenseRepository.findById(id).map(ExpenseResponseDto::new)
                .orElseThrow(() -> {
                    log.warn("No se encontro un gasto con el ID: {}", id);
                    return new ExpenseNotFoundException("No se encontro un gasto con ese ID: " + id);
                });

    }

    @Override
    public Double totalExpenses(Long userId, int month) {

        log.info("Calculando el total de gastos para el usuario con ID: {} en el mes: {}", userId, month);

        if (month < 1 || month > 12) {
            log.warn("El mes proporcionado es inválido: {}", month);
            throw new InvalidExpenseDataException("El mes debe estar entre 1 y 12. Mes proporcionado: " + month);
        }

        List<Expense> expenses = expenseRepository.findByUserIdAndDateMonth(userId, month);

        if (expenses.isEmpty()) {
            log.warn("No se encontraron gastos para el usuario con ID: {} en el mes: {}", userId, month);
            throw new ExpenseNotFoundException("La lista de gastos del usuario se encuentra vacía este mes: " + month);
        }

        double total = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        log.info("El total de gastos para el usuario con ID: {} en el mes: {} es: {}", userId, month, total);
        return total;
    }

    @Override
    public Map<String, Double> percentage(Long userId, int month) {

        log.info("Calculando el porcentaje de gastos por categoría para el usuario con ID: {} en el mes: {}", userId, month);
        Double total = totalExpenses(userId, month);

        if (total == 0){
            log.warn("La lista de gasto esta vacia este mes.");
            throw new ExpenseNotFoundException("La lista de gastos del usuario se encuentra vacia este mes: " + month);
        }

        List<ExpenseResponseDto> expenses = findExpensesByuser(userId);

        Map<String, Double> percentages = expenses.stream()
                .collect(Collectors.groupingBy(
                        ExpenseResponseDto::getCategoryName,
                        Collectors.summingDouble(ExpenseResponseDto::getAmount)
                )).entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (entry.getValue() / total) * 100
                ));

        log.info("Porcentaje de gastos calculado: {}", percentages);
        return percentages;
    }

    @Override
    public ExpenseResponseDto addExpense(Long userId, ExpenseRequestDto expenseRequestDto) {

        log.info("Agregando un nuevo gasto para el usuario con ID: {}", userId);

        if(userId == null || userId <= 0){
            log.warn("El ID de usuario proporcionado es inválido para agregar un gasto: {}", userId);
            throw new InvalidExpenseDataException("El ID de usuario es invalido "+ userId);
        }


        try {
            Expense savedExpense = expenseRepository.save(expenseRequestDto.toEntity());
            log.info("Gasto agregado exitosamente: {}", savedExpense);
            return new ExpenseResponseDto(savedExpense);
        } catch (Exception e) {
            log.error("Error al guardar el gasto: {}", e.getMessage());
            throw new RuntimeException("Error al guardar el gasto. Por favor, inténtelo más tarde.", e);
        }
    }


    @Override
    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto expenseRequestDto) {

        log.info("Actualizando el gasto con ID: {}", id);
        if (id == null || id <= 0 ){
            log.warn("El ID del gasto proporcionado es inválido para editar: {}", id);
            throw new InvalidExpenseDataException("El ID del gasto es invalido "+ id);
        }

        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró un gasto con el ID para editar: {}", id);
                    return new ExpenseNotFoundException("No se encontró un gasto con ese ID: " + id);
                });

        Category category = new Category();
        category.setName(expenseRequestDto.getCategoryName());

        existingExpense.setCategory(category);
        existingExpense.setDate(expenseRequestDto.getDate());
        existingExpense.setAmount(expenseRequestDto.getAmount());
        existingExpense.setDescription(expenseRequestDto.getDescription());

        Expense updatedExpense = expenseRepository.save(existingExpense);
        log.info("Gasto actualizado exitosamente: {}", updatedExpense);
        return new ExpenseResponseDto(updatedExpense);
    }

    @Override
    public void  deleteExpense(Long id) {
        log.info("Eliminando el gasto con ID: {}", id);
        if (id == null || id <= 0) {
            log.warn("El ID del gasto proporcionado es inválido para eliminar: {}", id);
            throw new InvalidExpenseDataException("El ID del gasto es inválido " + id);
        }

        if (!expenseRepository.existsById(id)) {
            log.warn("No se encontró un gasto con el ID para eliminar: {}", id);
            throw new ExpenseNotFoundException("No se encontró el gasto con ID " + id);
        }

        expenseRepository.deleteById(id);
        log.info("Gasto eliminado exitosamente con ID: {}", id);
    }
}
