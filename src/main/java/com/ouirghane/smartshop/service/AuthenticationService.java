package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.LoginRequestDto;
import com.ouirghane.smartshop.dto.response.LoginResponseDto;

public interface AuthenticationService {
    
    LoginResponseDto login(LoginRequestDto request);

}