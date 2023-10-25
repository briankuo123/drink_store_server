package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.OrderRule;
import com.mycompany.myapp.repository.OrderRuleRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrderRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderRuleResourceIT {

    private static final Boolean DEFAULT_ORDER_STATUS = false;
    private static final Boolean UPDATED_ORDER_STATUS = true;

    private static final Integer DEFAULT_ORDER_FREQUENCY = 1;
    private static final Integer UPDATED_ORDER_FREQUENCY = 2;

    private static final String ENTITY_API_URL = "/api/order-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderRuleRepository orderRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderRuleMockMvc;

    private OrderRule orderRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderRule createEntity(EntityManager em) {
        OrderRule orderRule = new OrderRule().orderStatus(DEFAULT_ORDER_STATUS).orderFrequency(DEFAULT_ORDER_FREQUENCY);
        return orderRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderRule createUpdatedEntity(EntityManager em) {
        OrderRule orderRule = new OrderRule().orderStatus(UPDATED_ORDER_STATUS).orderFrequency(UPDATED_ORDER_FREQUENCY);
        return orderRule;
    }

    @BeforeEach
    public void initTest() {
        orderRule = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderRule() throws Exception {
        int databaseSizeBeforeCreate = orderRuleRepository.findAll().size();
        // Create the OrderRule
        restOrderRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderRule)))
            .andExpect(status().isCreated());

        // Validate the OrderRule in the database
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeCreate + 1);
        OrderRule testOrderRule = orderRuleList.get(orderRuleList.size() - 1);
        assertThat(testOrderRule.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testOrderRule.getOrderFrequency()).isEqualTo(DEFAULT_ORDER_FREQUENCY);
    }

    @Test
    @Transactional
    void createOrderRuleWithExistingId() throws Exception {
        // Create the OrderRule with an existing ID
        orderRule.setId(1L);

        int databaseSizeBeforeCreate = orderRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderRule)))
            .andExpect(status().isBadRequest());

        // Validate the OrderRule in the database
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRuleRepository.findAll().size();
        // set the field null
        orderRule.setOrderStatus(null);

        // Create the OrderRule, which fails.

        restOrderRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderRule)))
            .andExpect(status().isBadRequest());

        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderFrequencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRuleRepository.findAll().size();
        // set the field null
        orderRule.setOrderFrequency(null);

        // Create the OrderRule, which fails.

        restOrderRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderRule)))
            .andExpect(status().isBadRequest());

        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderRules() throws Exception {
        // Initialize the database
        orderRuleRepository.saveAndFlush(orderRule);

        // Get all the orderRuleList
        restOrderRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].orderFrequency").value(hasItem(DEFAULT_ORDER_FREQUENCY)));
    }

    @Test
    @Transactional
    void getOrderRule() throws Exception {
        // Initialize the database
        orderRuleRepository.saveAndFlush(orderRule);

        // Get the orderRule
        restOrderRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, orderRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderRule.getId().intValue()))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.booleanValue()))
            .andExpect(jsonPath("$.orderFrequency").value(DEFAULT_ORDER_FREQUENCY));
    }

    @Test
    @Transactional
    void getNonExistingOrderRule() throws Exception {
        // Get the orderRule
        restOrderRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderRule() throws Exception {
        // Initialize the database
        orderRuleRepository.saveAndFlush(orderRule);

        int databaseSizeBeforeUpdate = orderRuleRepository.findAll().size();

        // Update the orderRule
        OrderRule updatedOrderRule = orderRuleRepository.findById(orderRule.getId()).get();
        // Disconnect from session so that the updates on updatedOrderRule are not directly saved in db
        em.detach(updatedOrderRule);
        updatedOrderRule.orderStatus(UPDATED_ORDER_STATUS).orderFrequency(UPDATED_ORDER_FREQUENCY);

        restOrderRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrderRule))
            )
            .andExpect(status().isOk());

        // Validate the OrderRule in the database
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeUpdate);
        OrderRule testOrderRule = orderRuleList.get(orderRuleList.size() - 1);
        assertThat(testOrderRule.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrderRule.getOrderFrequency()).isEqualTo(UPDATED_ORDER_FREQUENCY);
    }

    @Test
    @Transactional
    void putNonExistingOrderRule() throws Exception {
        int databaseSizeBeforeUpdate = orderRuleRepository.findAll().size();
        orderRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRule in the database
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderRule() throws Exception {
        int databaseSizeBeforeUpdate = orderRuleRepository.findAll().size();
        orderRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRule in the database
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderRule() throws Exception {
        int databaseSizeBeforeUpdate = orderRuleRepository.findAll().size();
        orderRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderRule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderRule in the database
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderRuleWithPatch() throws Exception {
        // Initialize the database
        orderRuleRepository.saveAndFlush(orderRule);

        int databaseSizeBeforeUpdate = orderRuleRepository.findAll().size();

        // Update the orderRule using partial update
        OrderRule partialUpdatedOrderRule = new OrderRule();
        partialUpdatedOrderRule.setId(orderRule.getId());

        partialUpdatedOrderRule.orderStatus(UPDATED_ORDER_STATUS);

        restOrderRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderRule))
            )
            .andExpect(status().isOk());

        // Validate the OrderRule in the database
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeUpdate);
        OrderRule testOrderRule = orderRuleList.get(orderRuleList.size() - 1);
        assertThat(testOrderRule.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrderRule.getOrderFrequency()).isEqualTo(DEFAULT_ORDER_FREQUENCY);
    }

    @Test
    @Transactional
    void fullUpdateOrderRuleWithPatch() throws Exception {
        // Initialize the database
        orderRuleRepository.saveAndFlush(orderRule);

        int databaseSizeBeforeUpdate = orderRuleRepository.findAll().size();

        // Update the orderRule using partial update
        OrderRule partialUpdatedOrderRule = new OrderRule();
        partialUpdatedOrderRule.setId(orderRule.getId());

        partialUpdatedOrderRule.orderStatus(UPDATED_ORDER_STATUS).orderFrequency(UPDATED_ORDER_FREQUENCY);

        restOrderRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderRule))
            )
            .andExpect(status().isOk());

        // Validate the OrderRule in the database
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeUpdate);
        OrderRule testOrderRule = orderRuleList.get(orderRuleList.size() - 1);
        assertThat(testOrderRule.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrderRule.getOrderFrequency()).isEqualTo(UPDATED_ORDER_FREQUENCY);
    }

    @Test
    @Transactional
    void patchNonExistingOrderRule() throws Exception {
        int databaseSizeBeforeUpdate = orderRuleRepository.findAll().size();
        orderRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRule in the database
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderRule() throws Exception {
        int databaseSizeBeforeUpdate = orderRuleRepository.findAll().size();
        orderRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRule in the database
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderRule() throws Exception {
        int databaseSizeBeforeUpdate = orderRuleRepository.findAll().size();
        orderRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRuleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderRule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderRule in the database
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderRule() throws Exception {
        // Initialize the database
        orderRuleRepository.saveAndFlush(orderRule);

        int databaseSizeBeforeDelete = orderRuleRepository.findAll().size();

        // Delete the orderRule
        restOrderRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderRule> orderRuleList = orderRuleRepository.findAll();
        assertThat(orderRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
