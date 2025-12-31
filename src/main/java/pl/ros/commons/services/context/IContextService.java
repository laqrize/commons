package pl.ros.commons.services.context;

import pl.ros.commons.dtos.context.SecurityUser;

public interface IContextService {
    SecurityUser getCurrentUser();
    Long getCurrentUserId();
    String getCurrentUserEmail();
    boolean isAuthenticated();
    public void setSystemUserInContext();
}
