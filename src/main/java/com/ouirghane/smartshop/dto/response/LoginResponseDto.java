package com.ouirghane.smartshop.dto.response;

import com.ouirghane.smartshop.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

    private Long userId;
    private String username;
    private UserRole role;
    private String message;
    private boolean success;
}