package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.OrderDrink;
import com.mycompany.myapp.repository.OrderDrinkRepository;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link OrderDrinkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderDrinkResourceIT {

    private static final UUID DEFAULT_ORDER_ID = UUID.randomUUID();
    private static final UUID UPDATED_ORDER_ID = UUID.randomUUID();

    private static final UUID DEFAULT_DRINK_ID = UUID.randomUUID();
    private static final UUID UPDATED_DRINK_ID = UUID.randomUUID();

    private static final String DEFAULT_SUGAR = "AAAAAAAAAA";
    private static final String UPDATED_SUGAR = "BBBBBBBBBB";

    private static final String DEFAULT_ICE = "AAAAAAAAAA";
    private static final String UPDATED_ICE = "BBBBBBBBBB";

    private static final String DEFAULT_TOPPINGS = "AAAAAAAAAA";
    private static final String UPDATED_TOPPINGS = "BBBBBBBBBB";

    private static final String DEFAULT_DRINK_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_DRINK_SIZE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DRINK_PRICE = 1;
    private static final Integer UPDATED_DRINK_PRICE = 2;

    private static final String ENTITY_API_URL = "/api/order-drinks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderDrinkRepository orderDrinkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderDrinkMockMvc;

    private OrderDrink orderDrink;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderDrink createEntity(EntityManager em) {
        OrderDrink orderDrink = new OrderDrink()
            .orderId(DEFAULT_ORDER_ID)
            .drinkId(DEFAULT_DRINK_ID)
            .sugar(DEFAULT_SUGAR)
            .ice(DEFAULT_ICE)
            .toppings(DEFAULT_TOPPINGS)
            .drinkSize(DEFAULT_DRINK_SIZE)
            .drinkPrice(DEFAULT_DRINK_PRICE);
        return orderDrink;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderDrink createUpdatedEntity(EntityManager em) {
        OrderDrink orderDrink = new OrderDrink()
            .orderId(UPDATED_ORDER_ID)
            .drinkId(UPDATED_DRINK_ID)
            .sugar(UPDATED_SUGAR)
            .ice(UPDATED_ICE)
            .toppings(UPDATED_TOPPINGS)
            .drinkSize(UPDATED_DRINK_SIZE)
            .drinkPrice(UPDATED_DRINK_PRICE);
        return orderDrink;
    }

    @BeforeEach
    public void initTest() {
        orderDrink = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderDrink() throws Exception {
        int databaseSizeBeforeCreate = orderDrinkRepository.findAll().size();
        // Create the OrderDrink
        restOrderDrinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDrink)))
            .andExpect(status().isCreated());

        // Validate the OrderDrink in the database
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeCreate + 1);
        OrderDrink testOrderDrink = orderDrinkList.get(orderDrinkList.size() - 1);
        assertThat(testOrderDrink.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrderDrink.getDrinkId()).isEqualTo(DEFAULT_DRINK_ID);
        assertThat(testOrderDrink.getSugar()).isEqualTo(DEFAULT_SUGAR);
        assertThat(testOrderDrink.getIce()).isEqualTo(DEFAULT_ICE);
        assertThat(testOrderDrink.getToppings()).isEqualTo(DEFAULT_TOPPINGS);
        assertThat(testOrderDrink.getDrinkSize()).isEqualTo(DEFAULT_DRINK_SIZE);
        assertThat(testOrderDrink.getDrinkPrice()).isEqualTo(DEFAULT_DRINK_PRICE);
    }

    @Test
    @Transactional
    void createOrderDrinkWithExistingId() throws Exception {
        // Create the OrderDrink with an existing ID
        orderDrink.setId(1L);

        int databaseSizeBeforeCreate = orderDrinkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderDrinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDrink)))
            .andExpect(status().isBadRequest());

        // Validate the OrderDrink in the database
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDrinkRepository.findAll().size();
        // set the field null
        orderDrink.setOrderId(null);

        // Create the OrderDrink, which fails.

        restOrderDrinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDrink)))
            .andExpect(status().isBadRequest());

        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDrinkIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDrinkRepository.findAll().size();
        // set the field null
        orderDrink.setDrinkId(null);

        // Create the OrderDrink, which fails.

        restOrderDrinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDrink)))
            .andExpect(status().isBadRequest());

        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderDrinks() throws Exception {
        // Initialize the database
        orderDrinkRepository.saveAndFlush(orderDrink);

        // Get all the orderDrinkList
        restOrderDrinkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderDrink.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.toString())))
            .andExpect(jsonPath("$.[*].drinkId").value(hasItem(DEFAULT_DRINK_ID.toString())))
            .andExpect(jsonPath("$.[*].sugar").value(hasItem(DEFAULT_SUGAR)))
            .andExpect(jsonPath("$.[*].ice").value(hasItem(DEFAULT_ICE)))
            .andExpect(jsonPath("$.[*].toppings").value(hasItem(DEFAULT_TOPPINGS)))
            .andExpect(jsonPath("$.[*].drinkSize").value(hasItem(DEFAULT_DRINK_SIZE)))
            .andExpect(jsonPath("$.[*].drinkPrice").value(hasItem(DEFAULT_DRINK_PRICE)));
    }

    @Test
    @Transactional
    void getOrderDrink() throws Exception {
        // Initialize the database
        orderDrinkRepository.saveAndFlush(orderDrink);

        // Get the orderDrink
        restOrderDrinkMockMvc
            .perform(get(ENTITY_API_URL_ID, orderDrink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderDrink.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.toString()))
            .andExpect(jsonPath("$.drinkId").value(DEFAULT_DRINK_ID.toString()))
            .andExpect(jsonPath("$.sugar").value(DEFAULT_SUGAR))
            .andExpect(jsonPath("$.ice").value(DEFAULT_ICE))
            .andExpect(jsonPath("$.toppings").value(DEFAULT_TOPPINGS))
            .andExpect(jsonPath("$.drinkSize").value(DEFAULT_DRINK_SIZE))
            .andExpect(jsonPath("$.drinkPrice").value(DEFAULT_DRINK_PRICE));
    }

    @Test
    @Transactional
    void getNonExistingOrderDrink() throws Exception {
        // Get the orderDrink
        restOrderDrinkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderDrink() throws Exception {
        // Initialize the database
        orderDrinkRepository.saveAndFlush(orderDrink);

        int databaseSizeBeforeUpdate = orderDrinkRepository.findAll().size();

        // Update the orderDrink
        OrderDrink updatedOrderDrink = orderDrinkRepository.findById(orderDrink.getId()).get();
        // Disconnect from session so that the updates on updatedOrderDrink are not directly saved in db
        em.detach(updatedOrderDrink);
        updatedOrderDrink
            .orderId(UPDATED_ORDER_ID)
            .drinkId(UPDATED_DRINK_ID)
            .sugar(UPDATED_SUGAR)
            .ice(UPDATED_ICE)
            .toppings(UPDATED_TOPPINGS)
            .drinkSize(UPDATED_DRINK_SIZE)
            .drinkPrice(UPDATED_DRINK_PRICE);

        restOrderDrinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderDrink.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrderDrink))
            )
            .andExpect(status().isOk());

        // Validate the OrderDrink in the database
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeUpdate);
        OrderDrink testOrderDrink = orderDrinkList.get(orderDrinkList.size() - 1);
        assertThat(testOrderDrink.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderDrink.getDrinkId()).isEqualTo(UPDATED_DRINK_ID);
        assertThat(testOrderDrink.getSugar()).isEqualTo(UPDATED_SUGAR);
        assertThat(testOrderDrink.getIce()).isEqualTo(UPDATED_ICE);
        assertThat(testOrderDrink.getToppings()).isEqualTo(UPDATED_TOPPINGS);
        assertThat(testOrderDrink.getDrinkSize()).isEqualTo(UPDATED_DRINK_SIZE);
        assertThat(testOrderDrink.getDrinkPrice()).isEqualTo(UPDATED_DRINK_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingOrderDrink() throws Exception {
        int databaseSizeBeforeUpdate = orderDrinkRepository.findAll().size();
        orderDrink.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderDrinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDrink.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDrink))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDrink in the database
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderDrink() throws Exception {
        int databaseSizeBeforeUpdate = orderDrinkRepository.findAll().size();
        orderDrink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDrinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDrink))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDrink in the database
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderDrink() throws Exception {
        int databaseSizeBeforeUpdate = orderDrinkRepository.findAll().size();
        orderDrink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDrinkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDrink)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderDrink in the database
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderDrinkWithPatch() throws Exception {
        // Initialize the database
        orderDrinkRepository.saveAndFlush(orderDrink);

        int databaseSizeBeforeUpdate = orderDrinkRepository.findAll().size();

        // Update the orderDrink using partial update
        OrderDrink partialUpdatedOrderDrink = new OrderDrink();
        partialUpdatedOrderDrink.setId(orderDrink.getId());

        partialUpdatedOrderDrink.orderId(UPDATED_ORDER_ID).drinkId(UPDATED_DRINK_ID).sugar(UPDATED_SUGAR).drinkPrice(UPDATED_DRINK_PRICE);

        restOrderDrinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderDrink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderDrink))
            )
            .andExpect(status().isOk());

        // Validate the OrderDrink in the database
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeUpdate);
        OrderDrink testOrderDrink = orderDrinkList.get(orderDrinkList.size() - 1);
        assertThat(testOrderDrink.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderDrink.getDrinkId()).isEqualTo(UPDATED_DRINK_ID);
        assertThat(testOrderDrink.getSugar()).isEqualTo(UPDATED_SUGAR);
        assertThat(testOrderDrink.getIce()).isEqualTo(DEFAULT_ICE);
        assertThat(testOrderDrink.getToppings()).isEqualTo(DEFAULT_TOPPINGS);
        assertThat(testOrderDrink.getDrinkSize()).isEqualTo(DEFAULT_DRINK_SIZE);
        assertThat(testOrderDrink.getDrinkPrice()).isEqualTo(UPDATED_DRINK_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateOrderDrinkWithPatch() throws Exception {
        // Initialize the database
        orderDrinkRepository.saveAndFlush(orderDrink);

        int databaseSizeBeforeUpdate = orderDrinkRepository.findAll().size();

        // Update the orderDrink using partial update
        OrderDrink partialUpdatedOrderDrink = new OrderDrink();
        partialUpdatedOrderDrink.setId(orderDrink.getId());

        partialUpdatedOrderDrink
            .orderId(UPDATED_ORDER_ID)
            .drinkId(UPDATED_DRINK_ID)
            .sugar(UPDATED_SUGAR)
            .ice(UPDATED_ICE)
            .toppings(UPDATED_TOPPINGS)
            .drinkSize(UPDATED_DRINK_SIZE)
            .drinkPrice(UPDATED_DRINK_PRICE);

        restOrderDrinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderDrink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderDrink))
            )
            .andExpect(status().isOk());

        // Validate the OrderDrink in the database
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeUpdate);
        OrderDrink testOrderDrink = orderDrinkList.get(orderDrinkList.size() - 1);
        assertThat(testOrderDrink.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderDrink.getDrinkId()).isEqualTo(UPDATED_DRINK_ID);
        assertThat(testOrderDrink.getSugar()).isEqualTo(UPDATED_SUGAR);
        assertThat(testOrderDrink.getIce()).isEqualTo(UPDATED_ICE);
        assertThat(testOrderDrink.getToppings()).isEqualTo(UPDATED_TOPPINGS);
        assertThat(testOrderDrink.getDrinkSize()).isEqualTo(UPDATED_DRINK_SIZE);
        assertThat(testOrderDrink.getDrinkPrice()).isEqualTo(UPDATED_DRINK_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderDrink() throws Exception {
        int databaseSizeBeforeUpdate = orderDrinkRepository.findAll().size();
        orderDrink.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderDrinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderDrink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDrink))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDrink in the database
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderDrink() throws Exception {
        int databaseSizeBeforeUpdate = orderDrinkRepository.findAll().size();
        orderDrink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDrinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDrink))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDrink in the database
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderDrink() throws Exception {
        int databaseSizeBeforeUpdate = orderDrinkRepository.findAll().size();
        orderDrink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDrinkMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderDrink))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderDrink in the database
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderDrink() throws Exception {
        // Initialize the database
        orderDrinkRepository.saveAndFlush(orderDrink);

        int databaseSizeBeforeDelete = orderDrinkRepository.findAll().size();

        // Delete the orderDrink
        restOrderDrinkMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderDrink.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderDrink> orderDrinkList = orderDrinkRepository.findAll();
        assertThat(orderDrinkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
