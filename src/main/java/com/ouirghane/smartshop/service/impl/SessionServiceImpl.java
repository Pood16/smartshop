package com.ouirghane.smartshop.service.impl;

import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.enums.UserRole;
import com.ouirghane.smartshop.exception.AccessDeniedException;
import com.ouirghane.smartshop.exception.AuthenticationException;
import com.ouirghane.smartshop.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {

    private static final String USER_SESSION_KEY = "authenticated_user";

    @Override
    public User getAuthenticatedUser(HttpSession session) {
        User user = (User) session.getAttribute(USER_SESSION_KEY);
        if (user == null) {
            throw new AuthenticationException("User not authenticated");
        }
        return user;
    }

    @Override
    public Long getAuthenticatedUserId(HttpSession session) {
        return getAuthenticatedUser(session).getId();
    }

    @Override
    public UserRole getAuthenticatedUserRole(HttpSession session) {
        return getAuthenticatedUser(session).getRole();
    }

    @Override
    public boolean hasRole(HttpSession session, UserRole role) {
        try {
            return getAuthenticatedUserRole(session) == role;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public void validateAuthentication(HttpSession session) {
        if (session.getAttribute(USER_SESSION_KEY) == null) {
            throw new AuthenticationException("Authentication required");
        }
    }

    @Override
    public void validateAdminRole(HttpSession session) {
        validateAuthentication(session);
        if (!hasRole(session, UserRole.ADMIN)) {
            throw new AccessDeniedException("Admin access required");
        }
    }
}