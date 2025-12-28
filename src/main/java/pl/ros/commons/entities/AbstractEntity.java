package pl.ros.commons.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
//@MappedSuperclass
public abstract class AbstractEntity extends AbstractCustomEntity<Long>{

}
