package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OrderDrink;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderDrink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderDrinkRepository extends JpaRepository<OrderDrink, Long> {
    List<OrderDrink> getOrderDrinkByDrinkId(UUID drinkId);

    void deleteAllByOrderId(UUID orderId);
}
