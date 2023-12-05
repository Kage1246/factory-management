package edu.hust.hespi.factorymanagement.repository;

import edu.hust.hespi.factorymanagement.model.entity.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public interface AttributeRepository extends JpaRepository<AttributeEntity, Long> {
    Optional<AttributeEntity> findByIdAndIsActiveTrue(Long id);
    List<AttributeEntity> findAllByEntityTypeAndIsActiveTrue(Integer entityType);
    List<AttributeEntity> findAllByEntityTypeAndColumnTypeAndIsActiveTrue(Integer entityType, Integer columnType);
     default Map<String, AttributeEntity> findAllByEntityTypeAndColumnTypeToMap(Integer entityType, Integer columnType) {
        return findAllByEntityTypeAndColumnTypeAndIsActiveTrue(entityType, columnType)
                .stream().collect(Collectors.toMap(AttributeEntity::getColumnName, Function.identity()));
    }
    List<AttributeEntity> findAllByEntityTypeAndColumnNameInAndIsActiveTrue(Integer entityType, Collection<String> keyName);
}
