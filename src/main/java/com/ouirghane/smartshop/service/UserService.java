package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.UserCreateRequestDto;
import com.ouirghane.smartshop.dto.response.UserResponseDto;
import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.enums.UserRole;

public interface UserService {
    
    UserResponseDto createUser(UserCreateRequestDto request);
    
    User findByUsername(String username);
    
    User findByUsernameAndRole(String username, UserRole role);
    
    boolean existsByUsername(String username);
}