package com.app.ExpenseTracker.service.impl;

import com.app.ExpenseTracker.dto.request.UserRequestDto;
import com.app.ExpenseTracker.dto.response.UserResponseDto;
import com.app.ExpenseTracker.entity.User;
import com.app.ExpenseTracker.exception.UserNotFoundException;
import com.app.ExpenseTracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void findUserById() throws UserNotFoundException {

        Long userId = 1L;
        User user = new User(userId, "brian", "brian@hotmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
       User foundUser = userService.findUserById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        assertEquals("brian", foundUser.getName());
        assertEquals("brian@hotmail.com", foundUser.getEmail());
    }

    @Test
    void addUser() {

        UserRequestDto userRequestDto = new UserRequestDto("brian", "brian@hotmail.com");

        User user = new User(1L, "brian", "brian@hotmail.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto userResponseDto = userService.addUser(userRequestDto);

        assertNotNull(userResponseDto);
        assertEquals("brian", userResponseDto.getName());
        assertEquals("brian@hotmail.com", userResponseDto.getEmail());
        assertEquals(1L, userResponseDto.getId());

    }

    @Test
    void updateUser() throws UserNotFoundException {
        Long userId = 1L;
        UserRequestDto updatedRequest = new UserRequestDto("ariel", "ariel@gmail.com");

        User existingUser = new User(userId, "brian", "brian@hotmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        User updatedUser = new User(userId, "ariel", "ariel@gmail.com");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDto response = userService.updateUser(userId, updatedRequest);

        assertNotNull(response);
        assertEquals("ariel", response.getName());
        assertEquals("ariel@gmail.com", response.getEmail());
        assertEquals(userId, response.getId());
    }

    @Test
    void deleteUser() {
        UserRequestDto userRequestDto = new UserRequestDto("brian", "brian@hotmail.com");

        User user = userRequestDto.toEntity();

        doNothing().when(userRepository).deleteById(user.getId());

        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());

    }
}