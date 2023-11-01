package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Menu;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Menu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    public Menu findByDrinkId(UUID drinkId);
}
