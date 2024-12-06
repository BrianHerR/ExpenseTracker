package com.app.ExpenseTracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserResponseDto {

    private Long id;

    private String name;

    private String email;

}
