package com.ouirghane.smartshop.service.impl;

import com.ouirghane.smartshop.dto.request.LoginRequestDto;
import com.ouirghane.smartshop.dto.response.LoginResponseDto;
import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.exception.AuthenticationException;
import com.ouirghane.smartshop.repository.UserRepository;
import com.ouirghane.smartshop.security.CustomUserDetails;
import com.ouirghane.smartshop.security.JwtService;
import com.ouirghane.smartshop.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        return LoginResponseDto
                .builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .message("Login successful")
                .success(true)
                .build();
    }
}