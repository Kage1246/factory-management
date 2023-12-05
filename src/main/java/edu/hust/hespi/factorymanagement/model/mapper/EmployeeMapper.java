package edu.hust.hespi.factorymanagement.model.mapper;

import edu.hust.hespi.factorymanagement.model.dto.EmployeeDTO;
import edu.hust.hespi.factorymanagement.model.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, EmployeeEntity> {
    @Mapping(target = "employeeCode", ignore = true)
    void updateFromDTO(@MappingTarget EmployeeEntity entity, EmployeeDTO dto);
}
