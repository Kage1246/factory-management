package edu.hust.hespi.factorymanagement.model.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "value")
public class ValueEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "entity_type")
    private Integer entityType;
    @Basic
    @Column(name = "column_id")
    private Long columnId;
    @Basic
    @Column(name = "entity_id")
    private Long entityId;
    @Basic
    @Column(name = "common_value")
    private String commonValue;
    @Basic
    @Column(name = "integer_value")
    private Long integerValue;
    @Basic
    @Column(name = "double_value")
    private Double doubleValue;
    @Basic
    @Column(name = "string_value")
    private String stringValue;
    @Basic
    @Column(name = "time_value")
    private Timestamp timeValue;
    @Basic
    @Column(name = "boolean_value")
    private Boolean booleanValue;
    @Basic
    @Column(name = "is_active")
    private Boolean isActive = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getCommonValue() {
        return commonValue;
    }

    public void setCommonValue(String commonValue) {
        this.commonValue = commonValue;
    }

    public Long getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(Long integerValue) {
        this.integerValue = integerValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Timestamp getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(Timestamp timeValue) {
        this.timeValue = timeValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
