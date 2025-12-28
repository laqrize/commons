package pl.ros.commons.dtos.context;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


public record SecurityUser (
    Long id,
    String username,
    String email,
    Set<String> roles
){
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }
}
