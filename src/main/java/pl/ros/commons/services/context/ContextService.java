package pl.ros.commons.services.context;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.ros.commons.dtos.context.SecurityUser;

import java.util.ArrayList;
import java.util.HashSet;

@Component
public class ContextService implements IContextService{
    @Value("${system.user.email}")
    private String systemUserEmail;

    @Override
    public SecurityUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("No authentication in security context");
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof SecurityUser securityUser) {
            return securityUser;
        }
        throw new IllegalStateException(
                "Unexpected principal type: " + principal.getClass().getName() +
                        ". Expected SecurityUser."
        );
    }

    @Override
    public Long getCurrentUserId() {
        return getCurrentUser().id();
    }

    @Override
    public String getCurrentUserEmail() {
        return getCurrentUser().email();
    }

    @Override
    public boolean isAuthenticated() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth != null
                    && auth.isAuthenticated()
                    && auth.getPrincipal() instanceof SecurityUser;
        } catch (Exception e) {
            return false;
        }
    }

    public void setSystemUserInContext() {
        setUserInContext(1L, systemUserEmail);
    }

    public void setUserInContext(Long id, @NonNull String email) {
        SecurityUser user = new SecurityUser(id, null, email, new HashSet<>());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
