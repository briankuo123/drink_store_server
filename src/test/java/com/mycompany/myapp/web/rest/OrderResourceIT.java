package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.repository.OrderRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link OrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderResourceIT {

    private static final UUID DEFAULT_ORDER_ID = UUID.randomUUID();
    private static final UUID UPDATED_ORDER_ID = UUID.randomUUID();

    private static final UUID DEFAULT_USER_ID = UUID.randomUUID();
    private static final UUID UPDATED_USER_ID = UUID.randomUUID();

    private static final String DEFAULT_PAY_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_PAY_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_LOCATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_PRICE = 1;
    private static final Integer UPDATED_TOTAL_PRICE = 2;

    private static final Instant DEFAULT_DELIVERY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ORDER_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_COUPON = "AAAAAAAAAA";
    private static final String UPDATED_COUPON = "BBBBBBBBBB";

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .orderId(DEFAULT_ORDER_ID)
            .userId(DEFAULT_USER_ID)
            .payMethod(DEFAULT_PAY_METHOD)
            .deliveryLocation(DEFAULT_DELIVERY_LOCATION)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .deliveryTime(DEFAULT_DELIVERY_TIME)
            .orderStatus(DEFAULT_ORDER_STATUS)
            .coupon(DEFAULT_COUPON)
            .createBy(DEFAULT_CREATE_BY)
            .createDatetime(DEFAULT_CREATE_DATETIME)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDatetime(DEFAULT_LAST_MODIFIED_DATETIME);
        return order;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .orderId(UPDATED_ORDER_ID)
            .userId(UPDATED_USER_ID)
            .payMethod(UPDATED_PAY_METHOD)
            .deliveryLocation(UPDATED_DELIVERY_LOCATION)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .deliveryTime(UPDATED_DELIVERY_TIME)
            .orderStatus(UPDATED_ORDER_STATUS)
            .coupon(UPDATED_COUPON)
            .createBy(UPDATED_CREATE_BY)
            .createDatetime(UPDATED_CREATE_DATETIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();
        // Create the Order
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrder.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testOrder.getPayMethod()).isEqualTo(DEFAULT_PAY_METHOD);
        assertThat(testOrder.getDeliveryLocation()).isEqualTo(DEFAULT_DELIVERY_LOCATION);
        assertThat(testOrder.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testOrder.getDeliveryTime()).isEqualTo(DEFAULT_DELIVERY_TIME);
        assertThat(testOrder.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testOrder.getCoupon()).isEqualTo(DEFAULT_COUPON);
        assertThat(testOrder.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testOrder.getCreateDatetime()).isEqualTo(DEFAULT_CREATE_DATETIME);
        assertThat(testOrder.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOrder.getLastModifiedDatetime()).isEqualTo(DEFAULT_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void createOrderWithExistingId() throws Exception {
        // Create the Order with an existing ID
        order.setId(1L);

        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setOrderId(null);

        // Create the Order, which fails.

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setUserId(null);

        // Create the Order, which fails.

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].payMethod").value(hasItem(DEFAULT_PAY_METHOD)))
            .andExpect(jsonPath("$.[*].deliveryLocation").value(hasItem(DEFAULT_DELIVERY_LOCATION)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.[*].deliveryTime").value(hasItem(DEFAULT_DELIVERY_TIME.toString())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS)))
            .andExpect(jsonPath("$.[*].coupon").value(hasItem(DEFAULT_COUPON)))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createDatetime").value(hasItem(DEFAULT_CREATE_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDatetime").value(hasItem(DEFAULT_LAST_MODIFIED_DATETIME.toString())));
    }

    @Test
    @Transactional
    void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.payMethod").value(DEFAULT_PAY_METHOD))
            .andExpect(jsonPath("$.deliveryLocation").value(DEFAULT_DELIVERY_LOCATION))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE))
            .andExpect(jsonPath("$.deliveryTime").value(DEFAULT_DELIVERY_TIME.toString()))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS))
            .andExpect(jsonPath("$.coupon").value(DEFAULT_COUPON))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.createDatetime").value(DEFAULT_CREATE_DATETIME.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDatetime").value(DEFAULT_LAST_MODIFIED_DATETIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .orderId(UPDATED_ORDER_ID)
            .userId(UPDATED_USER_ID)
            .payMethod(UPDATED_PAY_METHOD)
            .deliveryLocation(UPDATED_DELIVERY_LOCATION)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .deliveryTime(UPDATED_DELIVERY_TIME)
            .orderStatus(UPDATED_ORDER_STATUS)
            .coupon(UPDATED_COUPON)
            .createBy(UPDATED_CREATE_BY)
            .createDatetime(UPDATED_CREATE_DATETIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);

        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrder.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testOrder.getPayMethod()).isEqualTo(UPDATED_PAY_METHOD);
        assertThat(testOrder.getDeliveryLocation()).isEqualTo(UPDATED_DELIVERY_LOCATION);
        assertThat(testOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrder.getDeliveryTime()).isEqualTo(UPDATED_DELIVERY_TIME);
        assertThat(testOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrder.getCoupon()).isEqualTo(UPDATED_COUPON);
        assertThat(testOrder.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testOrder.getCreateDatetime()).isEqualTo(UPDATED_CREATE_DATETIME);
        assertThat(testOrder.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrder.getLastModifiedDatetime()).isEqualTo(UPDATED_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void putNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, order.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .orderId(UPDATED_ORDER_ID)
            .payMethod(UPDATED_PAY_METHOD)
            .deliveryLocation(UPDATED_DELIVERY_LOCATION)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .deliveryTime(UPDATED_DELIVERY_TIME)
            .orderStatus(UPDATED_ORDER_STATUS)
            .coupon(UPDATED_COUPON)
            .createBy(UPDATED_CREATE_BY)
            .createDatetime(UPDATED_CREATE_DATETIME)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrder.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testOrder.getPayMethod()).isEqualTo(UPDATED_PAY_METHOD);
        assertThat(testOrder.getDeliveryLocation()).isEqualTo(UPDATED_DELIVERY_LOCATION);
        assertThat(testOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrder.getDeliveryTime()).isEqualTo(UPDATED_DELIVERY_TIME);
        assertThat(testOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrder.getCoupon()).isEqualTo(UPDATED_COUPON);
        assertThat(testOrder.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testOrder.getCreateDatetime()).isEqualTo(UPDATED_CREATE_DATETIME);
        assertThat(testOrder.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOrder.getLastModifiedDatetime()).isEqualTo(UPDATED_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void fullUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .orderId(UPDATED_ORDER_ID)
            .userId(UPDATED_USER_ID)
            .payMethod(UPDATED_PAY_METHOD)
            .deliveryLocation(UPDATED_DELIVERY_LOCATION)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .deliveryTime(UPDATED_DELIVERY_TIME)
            .orderStatus(UPDATED_ORDER_STATUS)
            .coupon(UPDATED_COUPON)
            .createBy(UPDATED_CREATE_BY)
            .createDatetime(UPDATED_CREATE_DATETIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrder.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testOrder.getPayMethod()).isEqualTo(UPDATED_PAY_METHOD);
        assertThat(testOrder.getDeliveryLocation()).isEqualTo(UPDATED_DELIVERY_LOCATION);
        assertThat(testOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrder.getDeliveryTime()).isEqualTo(UPDATED_DELIVERY_TIME);
        assertThat(testOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrder.getCoupon()).isEqualTo(UPDATED_COUPON);
        assertThat(testOrder.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testOrder.getCreateDatetime()).isEqualTo(UPDATED_CREATE_DATETIME);
        assertThat(testOrder.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrder.getLastModifiedDatetime()).isEqualTo(UPDATED_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void patchNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, order.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, order.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
