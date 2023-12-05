package edu.hust.hespi.factorymanagement.repository;

import edu.hust.hespi.factorymanagement.model.entity.ValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ValueRepository extends JpaRepository<ValueEntity, Long> {
    List<ValueEntity> findByEntityTypeAndEntityIdInAndIsActiveTrue(Integer entityType, Collection<Long> entityIds);
    Optional<ValueEntity> findByEntityIdAndColumnIdAndIsActiveTrue(Long entityId, Long columnId);
}
