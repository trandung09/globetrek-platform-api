package com.tvd.globetrekplatform.api.components;

import com.tvd.globetrekplatform.api.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtils {

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User loggedInUser) {
            if (loggedInUser.isActive()) {
                return loggedInUser;
            }
        }
        return null;
    }
}
