package com.luigi.pizza.service.mapper;

import com.luigi.pizza.persistence.entity.CustomerEntity;
import com.luigi.pizza.service.dto.CustomerDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    // Convierte de Entidad a DTO (Salida)
    CustomerDto toDto(CustomerEntity customerEntity);

    // Convierte de DTO a Entidad (Entrada/Guardado)
    CustomerEntity toEntity(CustomerDto customerDto);

    // Convierte colecciones completas
    List<CustomerDto> toDtoList(List<CustomerEntity> customers);
}