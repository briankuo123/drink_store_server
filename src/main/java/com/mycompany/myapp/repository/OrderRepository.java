package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Order;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(UUID userID);
    List<Order> findByUserIdAndOrderStatus(UUID userId, String orderStatus);
    List<Order> findAll();
    List<Order> findByOrderStatus(String orderStatus);
    List<Order> findByDeliveryTimeBetween(Instant start, Instant end);
    Order findByOrderId(UUID orderId);
    void deleteByOrderId(UUID orderId);
}
