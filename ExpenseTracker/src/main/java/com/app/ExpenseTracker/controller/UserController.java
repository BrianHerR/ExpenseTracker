package com.app.ExpenseTracker.controller;
import com.app.ExpenseTracker.dto.request.UserRequestDto;
import com.app.ExpenseTracker.dto.response.UserResponseDto;
import com.app.ExpenseTracker.entity.User;
import com.app.ExpenseTracker.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;



    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        try {
            log.info("Solicitud para obtener usuario con ID: {}", id);
            User user = userService.findUserById(id);
            log.info("Usuario con ID: {} recuperado exitosamente", id);
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            log.error("Error al obtener el usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener el usuario: " + e.getMessage());

        }
    }

    @PostMapping
    public ResponseEntity<?> postUser(@RequestBody @Valid UserRequestDto userRequestDto){
        try {
            log.info("Solicitud para crear un nuevo usuario");

            userService.addUser(userRequestDto);
            log.info("Usuario creado exitosamente con correo: {}", userRequestDto.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado exitosamente.");

        } catch (Exception e) {
            log.error("Error al crear el usuario: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequestDto userRequestDto){

        try {
            log.info("Solicitud para actualizar usuario con ID: {}", id);
            if (id == null || id <= 0) {
                log.warn("ID proporcionado no es válido: {}", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID proporcionado no es válido.");
            }




            UserResponseDto updatedUser = userService.updateUser(id ,userRequestDto);
            log.info("Usuario con ID: {} actualizado exitosamente", id);
            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
            log.error("Error al actualizar el usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body("Error al actualizar el usuario: " + e.getMessage());

        }
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {
            log.info("Solicitud para eliminar usuario con ID: {}", id);
            if (id == null || id <= 0) {
                log.warn("ID proporcionado no es válido: {}", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID proporcionado no es válido.");
            }

            userService.deleteUser(id);
            log.info("Usuario con ID: {} eliminado exitosamente", id);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            log.error("Error al eliminar el usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body("Error al eliminar el usuario: " + e.getMessage());

        }
    }

}
