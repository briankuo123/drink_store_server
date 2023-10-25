package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OrderRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRuleRepository extends JpaRepository<OrderRule, Long> {}
