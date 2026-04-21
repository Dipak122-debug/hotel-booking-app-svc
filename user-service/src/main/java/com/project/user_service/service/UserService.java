package com.project.user_service.service;

import com.project.user_service.dto.*;
import java.util.List;

public interface UserService {
    /**
     * Register a new user
     */
    UserResponseDto registerUser(UserRequestDto dto);

    /**
     * Authenticate user and return JWT token
     */
    AuthResponseDto authenticate(AuthRequestDto dto);

    /**
     * Get user by ID
     */
    UserResponseDto getUserById(Long userId);

    /**
     * Get all users
     */
    List<UserResponseDto> getAllUsers();


    UserResponseDto updateUser(Long userId, UserRequestDto dto);

    void deleteUser(Long userId);

    void updatePassword(Long userId, PasswordUpdateDto dto);

    String getUserStatus(Long userId);
}

