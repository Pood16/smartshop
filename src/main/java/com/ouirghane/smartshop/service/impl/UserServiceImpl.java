package com.ouirghane.smartshop.service.impl;

import com.ouirghane.smartshop.dto.request.UserCreateRequestDto;
import com.ouirghane.smartshop.dto.response.UserResponseDto;
import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.enums.UserRole;
import com.ouirghane.smartshop.exception.ResourceNotFoundException;
import com.ouirghane.smartshop.exception.ValidationException;
import com.ouirghane.smartshop.repository.UserRepository;
import com.ouirghane.smartshop.service.UserService;
import com.ouirghane.smartshop.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    @Override
    public UserResponseDto createUser(UserCreateRequestDto request) {
        if (existsByUsername(request.getUsername())) {
            throw new ValidationException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordUtil.hashPassword(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);

        return UserResponseDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .role(savedUser.getRole())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsernameAndRole(String username, UserRole role) {
        return userRepository.findByUsernameAndRole(username, role)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}