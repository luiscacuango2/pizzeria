package com.luigi.pizza.persistence.repository;

import com.luigi.pizza.persistence.entity.OrderEntity;
import com.luigi.pizza.persistence.projection.OrderSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends ListCrudRepository<OrderEntity, Integer>,
        PagingAndSortingRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByDeliveryMethodIn(List<String> deliveryMethods);

    // Consultar las Ordenes específicas de un usuario
    @Query(value = "SELECT * FROM pizza_order WHERE id_customer = :idCustomer", nativeQuery = true)
    List<OrderEntity> findCustomerOrders(@Param("idCustomer") Integer idCustomer);

    @Query(value =
        "SELECT po.id_order AS idOrder, " +
        "cu.name AS customerName, " +
        "po.order_date AS orderDate, " +
        "po.total_value AS orderTotal, " +
        "GROUP_CONCAT(pi.name) AS pizzaNames " +
        "FROM pizza_order po " +
        "INNER JOIN customer cu ON po.id_customer = cu.id_customer " +
        "INNER JOIN order_item oi ON po.id_order = oi.id_order " +
        "INNER JOIN pizza pi ON oi.id_pizza = pi.id_pizza " +
        "WHERE po.id_order = :idOrder " +
        "GROUP BY po.id_order, " +
        "cu.name, " +
        "po.order_date, " +
        "po.total_value", nativeQuery = true)
    OrderSummary findSummary (@Param("idOrder") Integer idOrder);

    /**
     * Procedimiento optimizado para Auditoría Total.
     * v_username asegura que sepamos quién ejecutó la promoción desde la DB.
     */
    @Procedure(value = "take_random_pizza_order", outputParameterName = "order_taken")
    boolean saveRandomOrder(
            @Param("id_customer") String idCustomer,
            @Param("delivery_method") String deliveryMethod,
            @Param("v_username") String vUsername
    );

    // Añadimos EntityGraph para la paginación principal
    @Override
    @EntityGraph(attributePaths = {"customer", "items"})
    Page<OrderEntity> findAll(Pageable pageable);

    // Para consultas rápidas o reportes completos
    List<OrderEntity> findByIdCustomer(Integer idCustomer);

    // Añadimos EntityGraph para búsquedas por cliente
    @EntityGraph(attributePaths = {"customer", "items"})
    Page<OrderEntity> findByIdCustomer(Integer idCustomer, Pageable pageable);

    // Para las órdenes de hoy
    @EntityGraph(attributePaths = {"customer", "items"})
    List<OrderEntity> findAllByOrderDateAfter(LocalDateTime date);

    // Para generar reportes de auditoria por usuario y rango de fechas
    List<OrderEntity> findByCreatedByAndCreatedDateBetween(String createdBy, LocalDateTime start, LocalDateTime end);
}
