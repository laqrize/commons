package pl.ros.commons.converters;



import pl.ros.commons.dtos.AbstractCustomDto;
import pl.ros.commons.dtos.context.UserDto;
import pl.ros.commons.entities.AbstractCustomEntity;
import pl.ros.commons.enums.EntityStatus;

import static pl.ros.commons.utils.NpeUtils.getValue;


public interface IStandardRecordConverter<D extends AbstractCustomDto, E extends AbstractCustomEntity> extends IConverter<D, E> {

    default void setCommonFieldsToEntity(D dto, E entity) {
        entity.setId(dto.getId());
        entity.setCreatedById(getValue(() -> dto.getCreatedBy().getId()));
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setStatus(dto.getStatus());
        entity.setVersion(dto.getVersion());
    }

    default void setCommonFieldsToDTO(D dto, E entity) {
        dto.setId(entity.getId());
        dto.setCreatedBy(
                UserDto.builder()
                        .id(getValue(entity::getCreatedById))
                        .build()
        );
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setStatus(EntityStatus.fromCode(entity.getStatus()));
        dto.setVersion(entity.getVersion());
    }

}
