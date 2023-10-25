package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.OrderRule;
import com.mycompany.myapp.repository.OrderRuleRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.OrderRule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrderRuleResource {

    private final Logger log = LoggerFactory.getLogger(OrderRuleResource.class);

    private static final String ENTITY_NAME = "orderRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderRuleRepository orderRuleRepository;

    public OrderRuleResource(OrderRuleRepository orderRuleRepository) {
        this.orderRuleRepository = orderRuleRepository;
    }

    /**
     * {@code POST  /order-rules} : Create a new orderRule.
     *
     * @param orderRule the orderRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderRule, or with status {@code 400 (Bad Request)} if the orderRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-rules")
    public ResponseEntity<OrderRule> createOrderRule(@Valid @RequestBody OrderRule orderRule) throws URISyntaxException {
        log.debug("REST request to save OrderRule : {}", orderRule);
        if (orderRule.getId() != null) {
            throw new BadRequestAlertException("A new orderRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderRule result = orderRuleRepository.save(orderRule);
        return ResponseEntity
            .created(new URI("/api/order-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-rules/:id} : Updates an existing orderRule.
     *
     * @param id the id of the orderRule to save.
     * @param orderRule the orderRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderRule,
     * or with status {@code 400 (Bad Request)} if the orderRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-rules/{id}")
    public ResponseEntity<OrderRule> updateOrderRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderRule orderRule
    ) throws URISyntaxException {
        log.debug("REST request to update OrderRule : {}, {}", id, orderRule);
        if (orderRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderRule result = orderRuleRepository.save(orderRule);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-rules/:id} : Partial updates given fields of an existing orderRule, field will ignore if it is null
     *
     * @param id the id of the orderRule to save.
     * @param orderRule the orderRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderRule,
     * or with status {@code 400 (Bad Request)} if the orderRule is not valid,
     * or with status {@code 404 (Not Found)} if the orderRule is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderRule> partialUpdateOrderRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderRule orderRule
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderRule partially : {}, {}", id, orderRule);
        if (orderRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderRule> result = orderRuleRepository
            .findById(orderRule.getId())
            .map(existingOrderRule -> {
                if (orderRule.getOrderStatus() != null) {
                    existingOrderRule.setOrderStatus(orderRule.getOrderStatus());
                }
                if (orderRule.getOrderFrequency() != null) {
                    existingOrderRule.setOrderFrequency(orderRule.getOrderFrequency());
                }

                return existingOrderRule;
            })
            .map(orderRuleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderRule.getId().toString())
        );
    }

    /**
     * {@code GET  /order-rules} : get all the orderRules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderRules in body.
     */
    @GetMapping("/order-rules")
    public List<OrderRule> getAllOrderRules() {
        log.debug("REST request to get all OrderRules");
        return orderRuleRepository.findAll();
    }

    /**
     * {@code GET  /order-rules/:id} : get the "id" orderRule.
     *
     * @param id the id of the orderRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-rules/{id}")
    public ResponseEntity<OrderRule> getOrderRule(@PathVariable Long id) {
        log.debug("REST request to get OrderRule : {}", id);
        Optional<OrderRule> orderRule = orderRuleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orderRule);
    }

    /**
     * {@code DELETE  /order-rules/:id} : delete the "id" orderRule.
     *
     * @param id the id of the orderRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-rules/{id}")
    public ResponseEntity<Void> deleteOrderRule(@PathVariable Long id) {
        log.debug("REST request to delete OrderRule : {}", id);
        orderRuleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
