package com.luigi.pizza.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luigi.pizza.persistence.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pizza_order")
@Getter
@Setter
@NoArgsConstructor
public class OrderEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order", nullable = false)
    private Integer idOrder;

    @Column(name = "id_customer", nullable = false)
    private Integer idCustomer;

    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_value", nullable = false, precision = 6, scale = 2)
    private BigDecimal totalValue;

    @Column(name = "delivery_method", nullable = false, columnDefinition = "CHAR(1)")
    private String deliveryMethod;

    @Column(name = "additional_notes", length = 200)
    private String additionalNotes;

    // saber en qué fase se encuentra el pedido
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    // --- RELACIONES OPTIMIZADAS ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer", referencedColumnName = "id_customer", insertable = false, updatable = false)
    @JsonIgnore
    private CustomerEntity customer;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("price DESC")
    private List<OrderItemEntity> items;

    // --- AYUDAS PARA AUDITORÍA Y CICLO DE VIDA ---
    @PrePersist
    public void prePersist() {
        if (this.orderDate == null) {
            this.orderDate = LocalDateTime.now();
        }
    }
}