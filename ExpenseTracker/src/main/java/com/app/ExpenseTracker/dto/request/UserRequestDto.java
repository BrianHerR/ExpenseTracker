package com.app.ExpenseTracker.dto.request;

import com.app.ExpenseTracker.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "El nombre no puede estar vacio.")
    private String name;

    @NotBlank(message = "El correo electronico no puede estar vacio.")
    @Email(message = "Debe proporcionar un correo electronico valido.")
    private String email;


    public User toEntity() {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
