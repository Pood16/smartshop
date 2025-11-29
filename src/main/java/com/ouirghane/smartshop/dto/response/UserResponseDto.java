package com.ouirghane.smartshop.dto.response;

import com.ouirghane.smartshop.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String username;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}