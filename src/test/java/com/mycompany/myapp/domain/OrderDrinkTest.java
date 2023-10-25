package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderDrinkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderDrink.class);
        OrderDrink orderDrink1 = new OrderDrink();
        orderDrink1.setId(1L);
        OrderDrink orderDrink2 = new OrderDrink();
        orderDrink2.setId(orderDrink1.getId());
        assertThat(orderDrink1).isEqualTo(orderDrink2);
        orderDrink2.setId(2L);
        assertThat(orderDrink1).isNotEqualTo(orderDrink2);
        orderDrink1.setId(null);
        assertThat(orderDrink1).isNotEqualTo(orderDrink2);
    }
}
