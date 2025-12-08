package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.LoginRequestDto;
import com.ouirghane.smartshop.dto.response.LoginResponseDto;
import com.ouirghane.smartshop.dto.response.UserResponseDto;
import jakarta.servlet.http.HttpSession;

import java.util.function.Function;

public interface AuthenticationService {
    
    LoginResponseDto login(LoginRequestDto request, HttpSession session);
    
    void logout(HttpSession session);
    
    UserResponseDto getCurrentUser(HttpSession session);
    
    boolean isAuthenticated(HttpSession session);

}