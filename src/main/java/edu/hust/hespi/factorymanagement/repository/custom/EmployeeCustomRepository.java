package edu.hust.hespi.factorymanagement.repository.custom;

import edu.hust.hespi.factorymanagement.model.dto.EmployeeDTO;
import edu.hust.hespi.factorymanagement.model.entity.EmployeeEntity;
import edu.hust.hespi.factorymanagement.model.input.PageFilterInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeCustomRepository {
    Page<EmployeeEntity> search(PageFilterInput<EmployeeDTO> input, Pageable pageable);
}
