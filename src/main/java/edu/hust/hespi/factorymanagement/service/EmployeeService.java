package edu.hust.hespi.factorymanagement.service;

import com.google.api.client.http.HttpStatusCodes;
import edu.hust.hespi.factorymanagement.exception.CustomException;
import edu.hust.hespi.factorymanagement.model.dto.EmployeeDTO;
import edu.hust.hespi.factorymanagement.model.entity.EmployeeEntity;
import edu.hust.hespi.factorymanagement.model.entity.ValueEntity;
import edu.hust.hespi.factorymanagement.model.input.PageFilterInput;
import edu.hust.hespi.factorymanagement.model.mapper.DtoMapper;
import edu.hust.hespi.factorymanagement.model.mapper.EmployeeMapper;
import edu.hust.hespi.factorymanagement.model.response.PageResponse;
import edu.hust.hespi.factorymanagement.repository.AttributeRepository;
import edu.hust.hespi.factorymanagement.repository.EmployeeRepository;
import edu.hust.hespi.factorymanagement.repository.ValueRepository;
import edu.hust.hespi.factorymanagement.repository.custom.EmployeeCustomRepository;
import edu.hust.hespi.factorymanagement.util.Constant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ValueRepository valueRepository;
    @Autowired
    AttributeRepository attributeRepository;
    @Autowired
    EmployeeCustomRepository employeeCustomRepository;
    @Autowired
    ValueService valueService;
    @Autowired
    DtoMapper dtoMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    public PageResponse<List<EmployeeDTO>> search(PageFilterInput<EmployeeDTO> input) {
        Pageable pageable = Pageable.unpaged();
        if (input.getPageSize() != 0) {
            pageable = PageRequest.of(input.getPageNumber(), input.getPageSize());
        }
        Page<EmployeeEntity> entityPage = employeeCustomRepository.search(input, pageable);
        List<EmployeeEntity> entities = entityPage.getContent();
        Map<Long, EmployeeEntity> entityMap = entities
                .stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, Function.identity()));
        List<ValueEntity> properties = valueRepository.findByEntityTypeAndEntityIdInAndIsActiveTrue(
                Constant.EntityType.EMPLOYEE,
                entityMap.keySet()
        );
        Map<Long, List<ValueEntity>> propertiesMap = properties.stream()
                .collect(Collectors.groupingBy(ValueEntity::getEntityId));
        List<EmployeeDTO> resultList = new ArrayList<>();
        for (EmployeeEntity employeeEntity : entityPage.getContent()) {
            EmployeeDTO dto = new EmployeeDTO();
            resultList.add((EmployeeDTO) dtoMapper.toDTO(dto, propertiesMap.get(employeeEntity.getEmployeeId())));
            BeanUtils.copyProperties(employeeEntity, dto, "isActive");
        }
        return new PageResponse<List<EmployeeDTO>>(entityPage.getTotalElements()).success().data(resultList);
    }

    @Transactional(rollbackFor = Exception.class)
    public EmployeeDTO create(EmployeeDTO dto) throws CustomException {
        if (dto.getEmployeeCode() == null) throw new CustomException(HttpStatusCodes.STATUS_CODE_BAD_REQUEST, "employee code cannot be empty");
        Optional<EmployeeEntity> optional = employeeRepository.findByEmployeeCodeAndIsActiveTrue(dto.getEmployeeCode());
        if (optional.isPresent()) throw new CustomException(HttpStatusCodes.STATUS_CODE_CONFLICT, "duplicate employee code " + dto.getEmployeeCode());
        EmployeeEntity toSaveEntity = employeeMapper.toEntity(dto);
        EmployeeEntity savedEntity = employeeRepository.save(toSaveEntity);
        dto.setEmployeeId(savedEntity.getEmployeeId());
        if (!dto.getPropertiesMap().isEmpty()) {
            List<ValueEntity> valueEntities = valueService.createOrUpdateKeyValueEntity(
                    savedEntity.getEmployeeId(),
                    dto.getPropertiesMap(),
                    Constant.EntityType.EMPLOYEE
            );
            valueRepository.saveAll(valueEntities);
        }
        return dto;
    }
}
