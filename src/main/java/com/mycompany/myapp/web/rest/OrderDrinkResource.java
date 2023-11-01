package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.OrderDrink;
import com.mycompany.myapp.repository.OrderDrinkRepository;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.OrderDrink}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrderDrinkResource {

    private final Logger log = LoggerFactory.getLogger(OrderDrinkResource.class);

    private static final String ENTITY_NAME = "orderDrink";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderDrinkRepository orderDrinkRepository;

    private final OrderRepository orderRepository;

    public OrderDrinkResource(OrderDrinkRepository orderDrinkRepository, OrderRepository orderRepository) {
        this.orderDrinkRepository = orderDrinkRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * {@code POST  /order-drinks} : Create a new orderDrink.
     *
     * @param orderDrink the orderDrink to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderDrink, or with status {@code 400 (Bad Request)} if the orderDrink has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-drinks")
    public ResponseEntity<OrderDrink> createOrderDrink(@Valid @RequestBody OrderDrink orderDrink) throws URISyntaxException {
        log.debug("REST request to save OrderDrink : {}", orderDrink);
        if (orderDrink.getId() != null) {
            throw new BadRequestAlertException("A new orderDrink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderDrink result = orderDrinkRepository.save(orderDrink);
        return ResponseEntity
            .created(new URI("/api/order-drinks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-drinks/:id} : Updates an existing orderDrink.
     *
     * @param id the id of the orderDrink to save.
     * @param orderDrink the orderDrink to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderDrink,
     * or with status {@code 400 (Bad Request)} if the orderDrink is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderDrink couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-drinks/{id}")
    public ResponseEntity<OrderDrink> updateOrderDrink(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderDrink orderDrink
    ) throws URISyntaxException {
        log.debug("REST request to update OrderDrink : {}, {}", id, orderDrink);
        if (orderDrink.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderDrink.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderDrinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderDrink result = orderDrinkRepository.save(orderDrink);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderDrink.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-drinks/:id} : Partial updates given fields of an existing orderDrink, field will ignore if it is null
     *
     * @param id the id of the orderDrink to save.
     * @param orderDrink the orderDrink to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderDrink,
     * or with status {@code 400 (Bad Request)} if the orderDrink is not valid,
     * or with status {@code 404 (Not Found)} if the orderDrink is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderDrink couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-drinks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderDrink> partialUpdateOrderDrink(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderDrink orderDrink
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderDrink partially : {}, {}", id, orderDrink);
        if (orderDrink.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderDrink.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderDrinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderDrink> result = orderDrinkRepository
            .findById(orderDrink.getId())
            .map(existingOrderDrink -> {
                if (orderDrink.getOrderId() != null) {
                    existingOrderDrink.setOrderId(orderDrink.getOrderId());
                }
                if (orderDrink.getDrinkId() != null) {
                    existingOrderDrink.setDrinkId(orderDrink.getDrinkId());
                }
                if (orderDrink.getSugar() != null) {
                    existingOrderDrink.setSugar(orderDrink.getSugar());
                }
                if (orderDrink.getIce() != null) {
                    existingOrderDrink.setIce(orderDrink.getIce());
                }
                if (orderDrink.getToppings() != null) {
                    existingOrderDrink.setToppings(orderDrink.getToppings());
                }
                if (orderDrink.getDrinkSize() != null) {
                    existingOrderDrink.setDrinkSize(orderDrink.getDrinkSize());
                }
                if (orderDrink.getDrinkPrice() != null) {
                    existingOrderDrink.setDrinkPrice(orderDrink.getDrinkPrice());
                }

                return existingOrderDrink;
            })
            .map(orderDrinkRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderDrink.getId().toString())
        );
    }

    /**
     * {@code GET  /order-drinks} : get all the orderDrinks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderDrinks in body.
     */
    @GetMapping("/order-drinks")
    public List<OrderDrink> getAllOrderDrinks() {
        log.debug("REST request to get all OrderDrinks");
        return orderDrinkRepository.findAll();
    }

    /**
     * {@code GET  /order-drinks/:id} : get the "id" orderDrink.
     *
     * @param id the id of the orderDrink to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderDrink, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-drinks/{id}")
    public ResponseEntity<OrderDrink> getOrderDrink(@PathVariable Long id) {
        log.debug("REST request to get OrderDrink : {}", id);
        Optional<OrderDrink> orderDrink = orderDrinkRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orderDrink);
    }

    /**
     * {@code DELETE  /order-drinks/:id} : delete the "id" orderDrink.
     *
     * @param id the id of the orderDrink to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-drinks/{id}")
    public ResponseEntity<Void> deleteOrderDrink(@PathVariable Long id) {
        log.debug("REST request to delete OrderDrink : {}", id);
        orderDrinkRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    //get all orders by userId
    @GetMapping("/userOrders/{userId}")
    public ResponseEntity getUserOrders(@PathVariable UUID userId) {
        List<OrderDrink> response = new ArrayList<>();
        List<Order> orders = orderRepository.findByUserId(userId);
        for (Order o : orders) {
            for (OrderDrink od : orderDrinkRepository.getOrderDrinkByDrinkId(o.getOrderId())) {
                response.add(od);
            }
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
