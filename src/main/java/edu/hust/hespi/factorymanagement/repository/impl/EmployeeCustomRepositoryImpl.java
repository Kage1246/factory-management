package edu.hust.hespi.factorymanagement.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.hust.hespi.factorymanagement.model.dto.EmployeeDTO;
import edu.hust.hespi.factorymanagement.model.entity.*;
import edu.hust.hespi.factorymanagement.model.input.PageFilterInput;
import edu.hust.hespi.factorymanagement.repository.AttributeRepository;
import edu.hust.hespi.factorymanagement.repository.custom.EmployeeCustomRepository;
import edu.hust.hespi.factorymanagement.util.AccentUtils;
import edu.hust.hespi.factorymanagement.util.Constant;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {
    @Autowired
    EntityManager entityManager;
    @Autowired
    AttributeRepository attributeRepository;
    private final Logger log = LoggerFactory.getLogger(EmployeeCustomRepositoryImpl.class);
    @Override
    public Page<EmployeeEntity> search(PageFilterInput<EmployeeDTO> input, Pageable pageable) {
        EmployeeDTO filter = input.getFilter();
        QEmployeeEntity qEmployeeEntity = QEmployeeEntity.employeeEntity;
        QValueEntity qValueEntity = QValueEntity.valueEntity;
        QAttributeEntity qAttributeEntity = QAttributeEntity.attributeEntity;

        // query common dynamic properties
        JPAQuery<Long> commonDynamicPropertiesQuery = new JPAQuery<>(entityManager)
                .select(qValueEntity.entityId).from(qValueEntity);
        BooleanBuilder commonDynamicBooleanBuilder = new BooleanBuilder();
        commonDynamicBooleanBuilder.and(qValueEntity.isActive.isTrue());
        if (!StringUtils.isEmpty(input.getCommon())) {
            commonDynamicBooleanBuilder.and(
                    AccentUtils.containsIgnoreAccent(qValueEntity.commonValue, input.getCommon())
            );
            commonDynamicPropertiesQuery.where(commonDynamicBooleanBuilder);
        }

        // main query
        JPAQuery<EmployeeEntity> query = new JPAQueryFactory(entityManager)
                .selectFrom(qEmployeeEntity)
                .leftJoin(qValueEntity)
                .on(qValueEntity.entityType.eq(Constant.EntityType.EMPLOYEE))
                .on(qEmployeeEntity.employeeId.eq(qValueEntity.entityId));

        if (pageable.isPaged()) {
            query.limit(pageable.getPageSize()).offset(pageable.getOffset());
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qEmployeeEntity.isActive.isTrue());

        // Search common
        if (!StringUtils.isEmpty(input.getCommon())) {
            booleanBuilder.and(
                    AccentUtils.containsIgnoreAccent(qEmployeeEntity.employeeCode, input.getCommon())
                            .or(AccentUtils.containsIgnoreAccent(qEmployeeEntity.username, input.getCommon()))
                            .or(AccentUtils.containsIgnoreAccent(qEmployeeEntity.name, input.getCommon()))
                            .or(AccentUtils.containsIgnoreAccent(qEmployeeEntity.phone, input.getCommon()))
                            .or(AccentUtils.containsIgnoreAccent(qEmployeeEntity.email, input.getCommon()))
                            .or(AccentUtils.containsIgnoreAccent(qEmployeeEntity.note, input.getCommon()))
                            .or(qValueEntity.id.in(commonDynamicPropertiesQuery))
            );
        }

        // Search static properties
        if (filter.getEmployeeId() != null) {
            booleanBuilder.and(qEmployeeEntity.employeeId.eq(filter.getEmployeeId()));
        }
        if (!StringUtils.isEmpty(filter.getEmployeeCode())) {
            booleanBuilder.and(AccentUtils.containsIgnoreAccent(qEmployeeEntity.employeeCode, filter.getEmployeeCode()));
        }
        if (!StringUtils.isEmpty(filter.getUsername())) {
            booleanBuilder.and(AccentUtils.containsIgnoreAccent(qEmployeeEntity.username, filter.getUsername()));
        }
        if (!StringUtils.isEmpty(filter.getName())) {
            booleanBuilder.and(AccentUtils.containsIgnoreAccent(qEmployeeEntity.name, filter.getName()));
        }
        if (!StringUtils.isEmpty(filter.getPhone())) {
            booleanBuilder.and(AccentUtils.containsIgnoreAccent(qEmployeeEntity.phone, filter.getPhone()));
        }
        if (!StringUtils.isEmpty(filter.getEmail())) {
            booleanBuilder.and(AccentUtils.containsIgnoreAccent(qEmployeeEntity.email, filter.getEmail()));
        }
        if (!StringUtils.isEmpty(filter.getNote())) {
            booleanBuilder.and(AccentUtils.containsIgnoreAccent(qEmployeeEntity.note, filter.getNote()));
        }
        if (filter.getStatus() != null) {
            booleanBuilder.and(qEmployeeEntity.status.eq(filter.getStatus()));
        }

        // Search dynamic properties
        if (!filter.getPropertiesMap().isEmpty()) {
            Map<String, AttributeEntity> columnEntitiesMap = attributeRepository.findAllByEntityTypeAndColumnTypeToMap(
                    Constant.EntityType.EMPLOYEE,
                    Constant.ColumnType.DYNAMIC
            );
            for (String columnName : filter.getPropertiesMap().keySet()) {
                JPAQuery<Long> dynamicPropertiesQuery = new JPAQuery<>(entityManager)
                        .select(qValueEntity.entityId)
                        .from(qValueEntity)
                        .join(qAttributeEntity)
                        .on(qValueEntity.columnId.eq(qAttributeEntity.id));
                BooleanBuilder dynamicBooleanBuilder = new BooleanBuilder();
                dynamicBooleanBuilder.and(qValueEntity.isActive.isTrue());
                String value = filter.getPropertiesMap().get(columnName);
                if (!StringUtils.isEmpty(value)) {
                    dynamicBooleanBuilder.and(qAttributeEntity.columnName.eq(columnName));
                    switch (columnEntitiesMap.get(columnName).getDataType()) {
                        case Constant.DataType.INTEGER:
                            dynamicBooleanBuilder.and(AccentUtils
                                    .containsIgnoreAccent((StringPath) qValueEntity.integerValue.stringValue(), value));
                            break;
                        case Constant.DataType.DOUBLE:
                            dynamicBooleanBuilder.and(AccentUtils
                                    .containsIgnoreAccent((StringPath) qValueEntity.doubleValue.stringValue(), value));
                            break;
                        case Constant.DataType.STRING:
                            dynamicBooleanBuilder.and(AccentUtils
                                    .containsIgnoreAccent(qValueEntity.stringValue, value));
                            break;
                        case Constant.DataType.TIME: // TODO: take note datetime
                            DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
                            String[] datetime = value.split(" ");
                            Timestamp startTime = Timestamp.valueOf(datetime[0]);
                            Timestamp endTime = Timestamp.valueOf(datetime[1]);
                            dynamicBooleanBuilder.and(qValueEntity.timeValue.between(startTime, endTime));
                            break;
                        case Constant.DataType.BOOLEAN:
                            dynamicBooleanBuilder.and(qValueEntity.booleanValue.stringValue().eq(value));
                            break;
                    }
                    dynamicPropertiesQuery.where(dynamicBooleanBuilder);
                    booleanBuilder.and(qEmployeeEntity.employeeId.in(dynamicPropertiesQuery));
                }
            }
        }

        // Sort
//        if (!StringUtils.isEmpty(input.getSortProperty())) {
//            List<AttributeEntity> columns =
//                    attributeRepository.findAllByEntityTypeAndIsActiveTrue(Constant.EntityType.EMPLOYEE);
//            boolean isSorted = false;
//            for (AttributeEntity column : columns) {
//                // Valid sort column name
//                if (input.getSortProperty().equals(column.getColumnName())) {
//                    isSorted = true;
//                    if (Objects.equals(column.getColumnType(), Constant.ColumnType.STATIC) ||
//                            Objects.equals(column.getColumnType(), Constant.ColumnType.FIXED)) {
//                        // Default col
//                        Path<Object> fieldPath = Expressions.path(Object.class, qEmployeeEntity, input.getSortProperty());
//                        query.orderBy(new OrderSpecifier(input.getSortOrder(), fieldPath));
//                    } else {
//                        // Dynamic col
//                        switch (column.getDataType()) {
//                            case Constant.DataType.INTEGER:
//                                query.orderBy(new OrderSpecifier<>(input.getSortOrder(), qValueEntity.integerValue));
//                                break;
//                            case Constant.DataType.DOUBLE:
//                                query.orderBy(new OrderSpecifier<>(input.getSortOrder(), qValueEntity.doubleValue));
//                                break;
//                            case Constant.DataType.STRING:
//                                query.orderBy(new OrderSpecifier<>(input.getSortOrder(), qValueEntity.stringValue));
//                                break;
//                            case Constant.DataType.TIME:
//                                query.orderBy(new OrderSpecifier<>(input.getSortOrder(), qValueEntity.timeValue));
//                                break;
//                            case Constant.DataType.BOOLEAN:
//                                query.orderBy(new OrderSpecifier<>(input.getSortOrder(), qValueEntity.booleanValue));
//                                break;
//                        }
//                    }
//                }
//            }
//            if (!isSorted) {
//                Path<Object> fieldPath = Expressions.path(Object.class, qEmployeeEntity, input.getSortProperty());
//                query.orderBy(new OrderSpecifier(input.getSortOrder(), fieldPath));
//            }
//        }
        query.where(booleanBuilder).groupBy(qEmployeeEntity.employeeId);
        log.debug(query.toString());
        List<EmployeeEntity> result = query.fetch();
        return new PageImpl<>(result, pageable, query.fetchCount());
    }
}
