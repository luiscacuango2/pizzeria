package com.luigi.pizza.persistence.entity;

import com.luigi.pizza.persistence.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_role")
@IdClass(UserRoleId.class)
@Getter
@Setter
@NoArgsConstructor
public class UserRoleEntity extends AuditableEntity implements Serializable {
    @Id
    @Column(nullable = false, length = 20)
    private String username;

    @Id
    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "granted_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime grantedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    private UserEntity user;

    // Método de ayuda para inicializar la fecha de concesión si no viene del DTO
    @PrePersist
    public void prePersist() {
        if (this.grantedDate == null) {
            this.grantedDate = LocalDateTime.now();
        }
    }


}
