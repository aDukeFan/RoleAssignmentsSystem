package ru.luckyskeet.usermanagment.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.luckyskeet.usermanagment.model.User;
import ru.luckyskeet.usermanagment.model.UserDtoRequest;
import ru.luckyskeet.usermanagment.model.UserDtoResponse;
import ru.luckyskeet.usermanagment.model.UserDtoToUpdate;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(getCurrentDateTime())")
    @Mapping(target = "updatedAt", expression = "java(getCurrentDateTime())")
    @Mapping(target = "accessLevel", ignore = true)
    User toSave(UserDtoRequest userDto);


    UserDtoResponse toSend(User user);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(getCurrentDateTime())")
    @Mapping(target = "accessLevel", ignore = true)
    User toUpdateUser(UserDtoToUpdate userDtoToUpdate, @MappingTarget User user);
}
