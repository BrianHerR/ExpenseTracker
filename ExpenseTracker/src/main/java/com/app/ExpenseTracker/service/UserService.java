package com.app.ExpenseTracker.service;

import com.app.ExpenseTracker.dto.request.UserRequestDto;
import com.app.ExpenseTracker.dto.response.UserResponseDto;
import com.app.ExpenseTracker.entity.User;
import com.app.ExpenseTracker.exception.UserNotFoundException;

public interface UserService {

    User findUserById(Long id) throws UserNotFoundException;

    UserResponseDto addUser(UserRequestDto userRequestDto);

    UserResponseDto updateUser(Long id ,UserRequestDto userRequestDto) throws UserNotFoundException;

    void deleteUser(Long id);
}
