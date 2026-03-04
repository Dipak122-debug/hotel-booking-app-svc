package com.project.user_service.service;


import com.project.user_service.dto.AuthRequestDto;
import com.project.user_service.dto.AuthResponseDto;
import com.project.user_service.dto.PasswordUpdateDto;
import com.project.user_service.dto.UserRequestDto;
import com.project.user_service.dto.UserResponseDto;
import com.project.user_service.entity.User;
import com.project.user_service.exception.InvalidCredentialsException;
import com.project.user_service.exception.UserNotFoundException;
import com.project.user_service.repository.UserRepository;
import com.project.user_service.util.JwtUtil;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDto registerUser(UserRequestDto dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("ROLE_USER");
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        return mapToDto(user);
    }


    @Override
    public AuthResponseDto authenticate(AuthRequestDto dto) {

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }



        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new AuthResponseDto(token);
    }


    @Override
    public UserResponseDto getUserById(Long userId) {
        return mapToDto(fetchUser(userId));
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserRequestDto dto) {
        User user = fetchUser(userId);
        user.setEmail(dto.getEmail());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return mapToDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = fetchUser(userId);
        user.setStatus("INACTIVE"); // soft delete
    }


    @Override
    public void updatePassword(Long userId, PasswordUpdateDto dto) {
        User user = fetchUser(userId);

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password mismatch");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    @Override
    public String getUserStatus(Long userId) {
        return fetchUser(userId).getStatus();
    }

    private User fetchUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}


