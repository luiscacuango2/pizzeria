package com.luigi.pizza.service;

import com.luigi.pizza.persistence.entity.OrderEntity;
import com.luigi.pizza.persistence.projection.OrderSummary;
import com.luigi.pizza.persistence.repository.OrderRepository;
import com.luigi.pizza.service.dto.OrderDto;
import com.luigi.pizza.service.dto.RandomOrderDto;
import com.luigi.pizza.service.mapper.OrderMapper;
import com.luigi.pizza.web.util.SanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final String DELIVERY = "D";
    private final String CARRYOUT = "C";
    private final String ON_SITE = "S";

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public Page<OrderDto> getAllOrder(Pageable pageable) {
        // 1. Buscamos las entidades de forma paginada
        Page<OrderEntity> entities = this.orderRepository.findAll(pageable);

        // 2. Mapeamos a DTO (Record) para asegurar la inmutabilidad y seguridad
        return entities.map(this.orderMapper::toDto);
    }

    public List<OrderDto> getTodayOrders() {
        LocalDateTime today = LocalDate.now().atTime(0, 0);
        return this.orderMapper.toDtoList(this.orderRepository.findAllByOrderDateAfter(today));
    }

    public List<OrderDto> getOutsideOrders() {
        List<String> deliveryMethods = List.of(DELIVERY, CARRYOUT); // Arrays.asList(DELIVERY, CARRYOUT);
        return this.orderMapper.toDtoList(this.orderRepository.findAllByDeliveryMethodIn(deliveryMethods));
    }

    @Secured("ROLE_ADMIN")
    public Page<OrderDto> getCustomerOrders(Integer idCustomer, Pageable pageable) {
        // 1. Buscamos las entidades usando el repositorio paginado
        Page<OrderEntity> entities = this.orderRepository.findByIdCustomer(idCustomer, pageable);

        // 2. Convertimos la página de entidades a una página de DTOs (Records)
        // El método .map() de Page mantiene la metadata de paginación
        return entities.map(this.orderMapper::toDto);
    }

    public OrderSummary getSummary(Integer idOrder) {
        return this.orderRepository.findSummary(idOrder);
    }

    @Transactional
    public Boolean saveRandomOrder(RandomOrderDto dto) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setIdCustomer(dto.idCustomer());
        orderEntity.setDeliveryMethod(dto.deliveryMethod());

        // Aquí sanitizamos antes de que JPA Auditing guarde el registro
        orderEntity.setAdditionalNotes(SanitizerUtil.sanitize(dto.note()));

        this.orderRepository.save(orderEntity);
        return true;
    }

    @Transactional
    public OrderDto save(OrderDto orderDto) {
        // 1. Convertir DTO (Record) a Entidad usando el mapper
        // El mapper debe estar configurado para ignorar los campos de auditoría al convertir a Entidad
        OrderEntity orderEntity = this.orderMapper.toEntity(orderDto);
        orderEntity.setStatus("PENDING"); // Estado inicial para nuevas órdenes

        // 2. Vincular los ítems con la orden (Indispensable para CascadeType.ALL)
        if (orderEntity.getItems() != null) {
            orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        }

        // 3. Persistir (Aquí JPA Auditing llena created_by y created_date)
        OrderEntity savedEntity = this.orderRepository.save(orderEntity);

        // 4. Devolver el DTO con los datos generados (ID, fecha de auditoría, etc.)
        return this.orderMapper.toDto(savedEntity);
    }

    @Transactional
    public OrderDto updateStatus(Integer idOrder, String status) {
        OrderEntity order = this.orderRepository.findById(idOrder)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Ahora el método setStatus sí existirá en la entidad
        order.setStatus(status);

        // Al guardar, el AuditingEntityListener registrará quién hizo el cambio
        OrderEntity updatedOrder = this.orderRepository.save(order);
        return this.orderMapper.toDto(updatedOrder);
    }
}
