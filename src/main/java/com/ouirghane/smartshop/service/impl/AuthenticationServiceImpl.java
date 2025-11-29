package com.ouirghane.smartshop.service.impl;

import com.ouirghane.smartshop.dto.request.LoginRequestDto;
import com.ouirghane.smartshop.dto.response.LoginResponseDto;
import com.ouirghane.smartshop.dto.response.UserResponseDto;
import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.exception.AuthenticationException;
import com.ouirghane.smartshop.repository.UserRepository;
import com.ouirghane.smartshop.service.AuthenticationService;
import com.ouirghane.smartshop.util.PasswordUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    private static final String USER_SESSION_KEY = "authenticated_user";

    @Override
    public LoginResponseDto login(LoginRequestDto request, HttpSession session) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        if (!passwordUtil.verifyPassword(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }

        session.setAttribute(USER_SESSION_KEY, user);

        return LoginResponseDto
                .builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .message("Login successful")
                .success(true)
                .build();
    }

    @Override
    public void logout(HttpSession session) {
        session.removeAttribute(USER_SESSION_KEY);
        session.invalidate();
    }

    @Override
    public UserResponseDto getCurrentUser(HttpSession session) {
        User user = getAuthenticatedUser(session);
        
        return UserResponseDto
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public boolean isAuthenticated(HttpSession session) {
        return session.getAttribute(USER_SESSION_KEY) != null;
    }

    private User getAuthenticatedUser(HttpSession session) {
        User user = (User) session.getAttribute(USER_SESSION_KEY);
        if (user == null) {
            throw new AuthenticationException("User not authenticated");
        }
        return user;
    }
}