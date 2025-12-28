package pl.ros.commons.dtos.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;

    public static UserDto from(SecurityUser user) {
        return UserDto.builder()
                .id(user.id())
                .username(user.username())
                .email(user.email())
                .build();
    }
}
