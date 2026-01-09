package com.luigi.pizza.persistence.repository;

import com.luigi.pizza.persistence.entity.OrderEntity;
import com.luigi.pizza.persistence.projection.OrderSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends ListCrudRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByOrderDateAfter(LocalDateTime date);
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
}
