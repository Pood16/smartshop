package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.enums.UserRole;
import jakarta.servlet.http.HttpSession;

public interface SessionService {
    
    User getAuthenticatedUser(HttpSession session);
    
    Long getAuthenticatedUserId(HttpSession session);
    
    UserRole getAuthenticatedUserRole(HttpSession session);
    
    boolean hasRole(HttpSession session, UserRole role);
    
    void validateAuthentication(HttpSession session);
    
    void validateAdminRole(HttpSession session);
}