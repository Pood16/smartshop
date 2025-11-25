package com.ouirghane.smartshop.controller;

import com.ouirghane.smartshop.dto.request.LoginRequestDto;
import com.ouirghane.smartshop.dto.response.LoginResponseDto;
import com.ouirghane.smartshop.dto.response.UserResponseDto;
import com.ouirghane.smartshop.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid
            @RequestBody LoginRequestDto request, HttpSession session
    ) {
        LoginResponseDto response = authenticationService.login(request, session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        authenticationService.logout(session);
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

}