package edu.hust.hespi.factorymanagement.model.mapper;

import edu.hust.hespi.factorymanagement.model.dto.BaseDynamicDTO;
import edu.hust.hespi.factorymanagement.model.entity.AttributeEntity;
import edu.hust.hespi.factorymanagement.model.entity.ValueEntity;
import edu.hust.hespi.factorymanagement.repository.AttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Component
public class DtoMapper {
    @Autowired
    AttributeRepository attributeRepository;
    public BaseDynamicDTO toDTO(BaseDynamicDTO unmappedDTO, List<ValueEntity> properties) {
        if (CollectionUtils.isEmpty(properties)) return unmappedDTO;
        for (ValueEntity property : properties) {
            Optional<AttributeEntity> attrOptional = attributeRepository.findByIdAndIsActiveTrue(property.getColumnId());
            if (attrOptional.isPresent()) {
                String columnName = attrOptional.get().getColumnName();
                if (property.getBooleanValue() != null) {
                    unmappedDTO.getPropertiesMap().put(columnName, String.valueOf(property.getBooleanValue()));
                }
                if (property.getIntegerValue() != null) {
                    unmappedDTO.getPropertiesMap().put(columnName, String.valueOf(property.getIntegerValue()));
                }
                if (property.getDoubleValue() != null) {
                    unmappedDTO.getPropertiesMap().put(columnName, String.valueOf(property.getDoubleValue()));
                }

                if (property.getStringValue() != null) {
                    unmappedDTO.getPropertiesMap().put(columnName, String.valueOf(property.getStringValue()));
                }

                if (property.getTimeValue() != null) {
                    unmappedDTO.getPropertiesMap().put(columnName, String.valueOf(property.getTimeValue()));
                }
            }
        }
        return unmappedDTO;
    }
}
