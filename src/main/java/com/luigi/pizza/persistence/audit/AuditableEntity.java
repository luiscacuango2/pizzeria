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
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    // Fecha que creo
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    // Usuario que Modifico
    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    // Fecha que modifico
    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

}
