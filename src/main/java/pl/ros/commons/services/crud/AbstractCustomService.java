package pl.ros.commons.services.crud;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;
import pl.ros.commons.converters.IStandardRecordConverter;
import pl.ros.commons.dtos.AbstractCustomDto;
import pl.ros.commons.entities.AbstractCustomEntity;
import pl.ros.commons.enums.CrudOperation;
import pl.ros.commons.enums.EntityStatus;
import pl.ros.commons.services.context.IContextService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
public abstract class AbstractCustomService<D extends AbstractCustomDto, E extends AbstractCustomEntity, ID> {

    @Autowired
    protected CrudRepository<E, ID> repository;

    @Autowired
    protected IStandardRecordConverter<D, E> converter;

    @Autowired
    protected IContextService contextService;

    @Value("${app.versioning.enabled:true}")
    protected boolean versioningEnabled;

    public D create(D dto) {
        validate(dto, CrudOperation.CREATE);
        E entity = converter.toEntity(dto);
        entity.updateState(contextService.getCurrentUserId(), CrudOperation.CREATE);
        entity = repository.save(entity);
        return converter.toDto(entity);
    }

    public D update(@NonNull ID id, D dto) {
        Assert.isTrue(id.equals(dto.getId()), "Id in path and body must be the same");
        validate(dto, CrudOperation.UPDATE);
        E entity = findById((ID) dto.getId());
        validateVersions(dto, entity);
        setEntityFields(entity, dto);
        entity.updateState(contextService.getCurrentUserId(), CrudOperation.UPDATE);
        entity = repository.save(entity);
        return converter.toDto(entity);
    }


    public void delete(@NonNull ID id) {
        E entity = findById(id);
        entity.setStatus(EntityStatus.DELETED);
        entity.updateState(contextService.getCurrentUserId(), CrudOperation.DELETE);
        repository.save(entity);
    }

    public E findById(@NonNull ID id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Entity ({}) with id {} not found", getEntityClass().getSimpleName(), id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        });
    }

    public D findDtoById(@NonNull ID id) {
        return converter.toDto(findById(id));
    }

    protected void validate(D dto, CrudOperation mode) {
        Assert.notNull(dto, "Object cannot be null");
        if (mode == CrudOperation.CREATE) {
            Assert.isNull(dto.getId(), "Id must be null");
        } else {
            Assert.notNull(dto.getId(), "Id cannot be null");

        }
    }

    protected Class<E> getEntityClass() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            Type[] arguments = parameterizedType.getActualTypeArguments();
            return (Class<E>) arguments[1];
        }
        throw new IllegalArgumentException("Cannot determine entity class");
    }
    protected abstract void setEntityFields(E entity, D dto);

    private void validateVersions(D dto, E entity) {
        if(versioningEnabled){
            Assert.isTrue(entity.getVersion().equals(dto.getVersion()), "Version mismatch");
        }
    }

}
