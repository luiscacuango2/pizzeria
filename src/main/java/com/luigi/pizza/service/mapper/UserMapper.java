package com.luigi.pizza.service.mapper;

import com.luigi.pizza.persistence.entity.UserEntity;
import com.luigi.pizza.persistence.entity.UserRoleEntity;
import com.luigi.pizza.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    UserDto toDto(UserEntity userEntity);

    @Named("mapRoles")
    default List<String> mapRoles(List<UserRoleEntity> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(UserRoleEntity::getRole) // Suponiendo que el campo se llama "role"
                .collect(Collectors.toList());
    }

    @Mapping(target = "password", ignore = true) // NUNCA mapeamos el password desde un DTO de salida
    @Mapping(target = "roles", ignore = true) // No actualizar roles desde un DTO de entrada
    UserEntity toEntity(UserDto userDto);
}