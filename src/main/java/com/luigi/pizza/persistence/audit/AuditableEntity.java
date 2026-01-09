package com.luigi.pizza.persistence.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditableEntity {

    // Usuario que Creo
    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private String createdBy;

    // Fecha que creo
    @Column(name = "created_date", updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    // Usuario que Modifico
    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

    // Fecha que modifico
    @LastModifiedBy
    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
