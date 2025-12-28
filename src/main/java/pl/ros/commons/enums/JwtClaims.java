package pl.ros.commons.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JwtClaims {
    USER_ID("userId"),
    EMAIL("email"),
    ROLES("roles")
        ;
    private final String name;
}
