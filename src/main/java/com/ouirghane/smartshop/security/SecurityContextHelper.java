package com.ouirghane.smartshop.security;

import com.ouirghane.smartshop.enums.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHelper {

    public CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        return null;
    }

    public Long getCurrentUserId() {
        CustomUserDetails userDetails = getCurrentUser();
        return userDetails != null ? userDetails.getId() : null;
    }

    public String getCurrentUsername() {
        CustomUserDetails userDetails = getCurrentUser();
        return userDetails != null ? userDetails.getUsername() : null;
    }

    public UserRole getCurrentUserRole() {
        CustomUserDetails userDetails = getCurrentUser();
        return userDetails != null ? userDetails.getRole() : null;
    }

    public boolean isAdmin() {
        UserRole role = getCurrentUserRole();
        return role == UserRole.ADMIN;
    }

    public boolean isClient() {
        UserRole role = getCurrentUserRole();
        return role == UserRole.CLIENT;
    }
}
