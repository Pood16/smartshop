package com.ouirghane.smartshop.controller;

import com.ouirghane.smartshop.dto.request.LoginRequestDto;
import com.ouirghane.smartshop.dto.response.LoginResponseDto;
import com.ouirghane.smartshop.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid
            @RequestBody LoginRequestDto request
    ) {
        LoginResponseDto response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

}