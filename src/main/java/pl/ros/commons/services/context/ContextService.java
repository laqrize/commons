package pl.ros.commons.services.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.ros.commons.dtos.context.SecurityUser;

@Component
public class ContextService implements IContextService{
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
}
