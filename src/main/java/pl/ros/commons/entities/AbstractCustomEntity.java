package pl.ros.commons.entities;

//import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.ros.commons.enums.CrudOperation;
import pl.ros.commons.enums.EntityStatus;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@MappedSuperclass
public abstract class AbstractCustomEntity<ID> {
    protected String status;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    protected Long createdById;
    protected Long updatedById;
    protected Long version;

    public abstract ID getId();

    public abstract void setId(ID id);

    public void updateState(Long operatorId, CrudOperation operation) {
        updateVersion(operation);
        switch (operation) {
            case CREATE:
                this.status = EntityStatus.CURRENT.getCode();
                this.createdAt = LocalDateTime.now();
                this.createdById = operatorId;
                break;
            case DELETE:
                this.updatedAt = LocalDateTime.now();
                this.status = EntityStatus.DELETED.getCode();
                break;
            case UPDATE:
                this.updatedAt = LocalDateTime.now();
                this.updatedById = operatorId;
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
    }

    public void setStatus(EntityStatus status) {
        this.status = status != null ? status.getCode() : null;
    }

    private void updateVersion(CrudOperation operation) {
        if (operation == CrudOperation.CREATE) {
            this.version = 1L;
        } else {
            this.version++;
        }
    }

}
