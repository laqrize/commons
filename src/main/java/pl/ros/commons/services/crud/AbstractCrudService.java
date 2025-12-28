package pl.ros.commons.services.crud;

import lombok.extern.slf4j.Slf4j;
import pl.ros.commons.dtos.AbstractDto;
import pl.ros.commons.entities.AbstractEntity;

@Slf4j
public abstract class AbstractCrudService<D extends AbstractDto, E extends AbstractEntity> extends AbstractCustomService<D,E, Long>  {
}
