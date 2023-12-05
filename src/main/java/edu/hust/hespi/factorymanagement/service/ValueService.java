package edu.hust.hespi.factorymanagement.service;

import com.google.api.client.http.HttpStatusCodes;
import edu.hust.hespi.factorymanagement.exception.CustomException;
import edu.hust.hespi.factorymanagement.model.entity.AttributeEntity;
import edu.hust.hespi.factorymanagement.model.entity.ValueEntity;
import edu.hust.hespi.factorymanagement.repository.AttributeRepository;
import edu.hust.hespi.factorymanagement.repository.ValueRepository;
import edu.hust.hespi.factorymanagement.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ValueService {
    private static final Logger log = LoggerFactory.getLogger(ValueService.class);
    @Autowired
    AttributeRepository attributeRepository;
    @Autowired
    ValueRepository valueRepository;
    public List<ValueEntity> createOrUpdateKeyValueEntity(Long entityId, Map<String, String> inputMap, int entityType) throws CustomException {
        List<AttributeEntity> columnPropertyEntities = attributeRepository.findAllByEntityTypeAndColumnNameInAndIsActiveTrue(
                entityType,
                inputMap.keySet()
        );
        Map<String, AttributeEntity> keyNameColumnMap = columnPropertyEntities
                .stream()
                .collect(Collectors.toMap(AttributeEntity::getColumnName, Function.identity()));
        List<ValueEntity> keyValueEntities = new ArrayList<>();
        for (String keyName : inputMap.keySet()) {
            Optional<ValueEntity> optionalValue = valueRepository.findByEntityIdAndColumnIdAndIsActiveTrue(
                    entityId,
                    keyNameColumnMap.get(keyName).getId()
            );
            ValueEntity keyValueEntity = new ValueEntity();
            if (optionalValue.isEmpty()) {
                keyValueEntity = new ValueEntity();
                keyValueEntity.setEntityType(entityType);
                keyValueEntity.setEntityId(entityId);
                keyValueEntity.setColumnId(keyNameColumnMap.get(keyName).getId());
            }

            int dataType = keyNameColumnMap.get(keyName).getDataType();
            try {
                if ((inputMap.get(keyName) == null)) {
                    keyValueEntity.setCommonValue(null);
                } else {
                    keyValueEntity.setCommonValue(inputMap.get(keyName));
                }
                if (dataType == Constant.DataType.INTEGER) {
                    keyValueEntity.setIntegerValue(Long.parseLong(inputMap.get(keyName)));
                } else if (dataType == Constant.DataType.DOUBLE) {
                    keyValueEntity.setDoubleValue(Double.parseDouble(inputMap.get(keyName)));
                } else if (dataType == Constant.DataType.STRING) {
                    keyValueEntity.setStringValue(inputMap.get(keyName));
                } else if (dataType == Constant.DataType.TIME) {
                    keyValueEntity.setTimeValue(Timestamp.valueOf(inputMap.get(keyName)));
                }
            } catch (Exception e) {
                log.error("Invalid data type", e);
                throw new CustomException(HttpStatusCodes.STATUS_CODE_BAD_REQUEST, "invalid datatype " + keyNameColumnMap.get(keyName).getColumnName());
            }

            keyValueEntities.add(keyValueEntity);
        }
        return keyValueEntities;
    }
}
