package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ShoppingCart;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShoppingCart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    public List<ShoppingCart> findByUserId(UUID userId);
}
