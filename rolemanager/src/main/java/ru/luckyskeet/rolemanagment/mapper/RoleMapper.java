package ru.luckyskeet.rolemanagment.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.luckyskeet.rolemanagment.model.Role;
import ru.luckyskeet.rolemanagment.model.RoleDtoRequest;
import ru.luckyskeet.rolemanagment.model.RoleDtoResponse;
import ru.luckyskeet.rolemanagment.model.RoleDtoToUpdate;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    Role toSave(RoleDtoRequest roleDtoRequest);

    RoleDtoResponse toSend(Role role);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "id", ignore = true)
    Role toUpdateRole(RoleDtoToUpdate roleDtoToUpdate, @MappingTarget Role role);
}
