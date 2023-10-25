package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderRule.class);
        OrderRule orderRule1 = new OrderRule();
        orderRule1.setId(1L);
        OrderRule orderRule2 = new OrderRule();
        orderRule2.setId(orderRule1.getId());
        assertThat(orderRule1).isEqualTo(orderRule2);
        orderRule2.setId(2L);
        assertThat(orderRule1).isNotEqualTo(orderRule2);
        orderRule1.setId(null);
        assertThat(orderRule1).isNotEqualTo(orderRule2);
    }
}
