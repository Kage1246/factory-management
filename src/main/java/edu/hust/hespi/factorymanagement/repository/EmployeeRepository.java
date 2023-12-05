package edu.hust.hespi.factorymanagement.repository;

import edu.hust.hespi.factorymanagement.model.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    Optional<EmployeeEntity> findByEmployeeCodeAndIsActiveTrue(String code);
}
