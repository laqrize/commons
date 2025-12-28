package pl.ros.commons.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.ros.commons.dtos.context.UserDto;
import pl.ros.commons.enums.EntityStatus;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractCustomDto<ID> {
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected UserDto createdBy;
    protected EntityStatus status;
    protected Long version;
    public abstract ID getId();
    public abstract void setId(ID id);

}
