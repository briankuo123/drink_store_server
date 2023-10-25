package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OrderDrink;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderDrink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderDrinkRepository extends JpaRepository<OrderDrink, Long> {}
