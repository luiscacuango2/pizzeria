package com.luigi.pizza.persistence.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter // Usamos Lombok para mantener la clase limpia
@Setter
@MappedSuperclass //JPA reconoce los campos en las hijas
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity implements Serializable {

    private static final long serialVersionUID = 1L; // Recomendado al usar Serializable

    // Usuario que Creo
    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 50)
    private String createdBy;

    // Fecha que creo
    @CreatedDate
    @Column(name = "created_date", updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDate;

    // Usuario que Modifico
    @LastModifiedBy
    @Column(name = "modified_by", length = 50)
    private String modifiedBy;

    // Fecha que modifico
    @LastModifiedDate
    @Column(name = "modified_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedDate;

}
