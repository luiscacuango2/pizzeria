package com.luigi.pizza.persistence.entity;

import com.luigi.pizza.persistence.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
public class CustomerEntity extends AuditableEntity {
    @Id
    @Column(name = "id_customer", nullable = false, length = 15)
    private Integer idCustomer;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 150)
    private String address;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

}
