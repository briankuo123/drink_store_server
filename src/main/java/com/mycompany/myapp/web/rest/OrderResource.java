package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.service.OrderService;
import com.mycompany.myapp.service.dto.*;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Order}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(OrderResource.class);

    private static final String ENTITY_NAME = "order";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    public OrderResource(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    /**
     * {@code POST  /orders} : Create a new order.
     *
     * @param order the order to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new order, or with status {@code 400 (Bad Request)} if the order has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to save Order : {}", order);
        if (order.getId() != null) {
            throw new BadRequestAlertException("A new order cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Order result = orderRepository.save(order);
        return ResponseEntity
            .created(new URI("/api/orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /orders/:id} : Updates an existing order.
     *
     * @param id the id of the order to save.
     * @param order the order to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated order,
     * or with status {@code 400 (Bad Request)} if the order is not valid,
     * or with status {@code 500 (Internal Server Error)} if the order couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Order order)
        throws URISyntaxException {
        log.debug("REST request to update Order : {}, {}", id, order);
        if (order.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, order.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Order result = orderRepository.save(order);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, order.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /orders/:id} : Partial updates given fields of an existing order, field will ignore if it is null
     *
     * @param id the id of the order to save.
     * @param order the order to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated order,
     * or with status {@code 400 (Bad Request)} if the order is not valid,
     * or with status {@code 404 (Not Found)} if the order is not found,
     * or with status {@code 500 (Internal Server Error)} if the order couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Order> partialUpdateOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Order order
    ) throws URISyntaxException {
        log.debug("REST request to partial update Order partially : {}, {}", id, order);
        if (order.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, order.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Order> result = orderRepository
            .findById(order.getId())
            .map(existingOrder -> {
                if (order.getOrderId() != null) {
                    existingOrder.setOrderId(order.getOrderId());
                }
                if (order.getUserId() != null) {
                    existingOrder.setUserId(order.getUserId());
                }
                if (order.getPayMethod() != null) {
                    existingOrder.setPayMethod(order.getPayMethod());
                }
                if (order.getDeliveryLocation() != null) {
                    existingOrder.setDeliveryLocation(order.getDeliveryLocation());
                }
                if (order.getTotalPrice() != null) {
                    existingOrder.setTotalPrice(order.getTotalPrice());
                }
                if (order.getDeliveryTime() != null) {
                    existingOrder.setDeliveryTime(order.getDeliveryTime());
                }
                if (order.getOrderStatus() != null) {
                    existingOrder.setOrderStatus(order.getOrderStatus());
                }
                if (order.getCoupon() != null) {
                    existingOrder.setCoupon(order.getCoupon());
                }
                if (order.getCreateBy() != null) {
                    existingOrder.setCreateBy(order.getCreateBy());
                }
                if (order.getCreateDatetime() != null) {
                    existingOrder.setCreateDatetime(order.getCreateDatetime());
                }
                if (order.getLastModifiedBy() != null) {
                    existingOrder.setLastModifiedBy(order.getLastModifiedBy());
                }
                if (order.getLastModifiedDatetime() != null) {
                    existingOrder.setLastModifiedDatetime(order.getLastModifiedDatetime());
                }

                return existingOrder;
            })
            .map(orderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, order.getId().toString())
        );
    }

    /**
     * {@code GET  /orders} : get all the orders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orders in body.
     */
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        log.debug("REST request to get all Orders");
        return orderRepository.findAll();
    }

    /**
     * {@code GET  /orders/:id} : get the "id" order.
     *
     * @param id the id of the order to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the order, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        Optional<Order> order = orderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(order);
    }

    /**
     * {@code DELETE  /orders/:id} : delete the "id" order.
     *
     * @param id the id of the order to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.debug("REST request to delete Order : {}", id);
        orderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/customerReadOrder")
    public ResponseEntity customerReadOrder(@RequestBody ReadOrderRequestDTO readOrderRequestDTO) {
        List<ReadOrderResponseDTO> response = new ArrayList<>();
        response = orderService.customerReadOrder(readOrderRequestDTO);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/adminReadOrder")
    public ResponseEntity adminReadOrder(@RequestBody ReadOrderRequestDTO readOrderRequestDTO) {
        List<ReadOrderResponseDTO> response = new ArrayList<>();
        response = orderService.adminReadOrder(readOrderRequestDTO);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    //顧客更新訂單資料(特定狀態且正卻使用者才可執行)
    @PostMapping("/customerUpdateOrder")
    public ResponseEntity customerUpdateOrder(@RequestBody UpdateOrderDTO updateOrderDTO) {
        Order order = orderRepository.findByOrderId(updateOrderDTO.getOrderId());

        if (order.getOrderStatus() == "處理中" && updateOrderDTO.getOrderDrinkListChanged()) {
            return new ResponseEntity("訂單正在處理中，無法修改飲料項目。", HttpStatus.BAD_REQUEST);
        }
        if (order.getOrderStatus() == "處理完成待取餐") {
            return new ResponseEntity("訂單已處裡完成，無法修改訂單。", HttpStatus.BAD_REQUEST);
        }
        if (order.getUserId() != updateOrderDTO.getUserId()) {
            return new ResponseEntity("訂單修改錯誤，不正確的使用者。", HttpStatus.BAD_REQUEST);
        }

        orderService.customerUpdateOrder(updateOrderDTO, order);

        return new ResponseEntity(HttpStatus.OK);
    }

    //顧客刪除訂單資料(特定狀態且正確使用者才可執行)
    @PostMapping("/customerDeleteOrder")
    public ResponseEntity customerDeleteOrder(@RequestBody DeleteOrderDTO deleteOrderDTO) {
        Order order = orderRepository.findByOrderId(deleteOrderDTO.getOrderId());
        if (order.getOrderStatus() == "處理中") {
            return new ResponseEntity("訂單正在處理中，無法刪除訂單。", HttpStatus.BAD_REQUEST);
        }
        if (order.getOrderStatus() == "處理完成待取餐") {
            return new ResponseEntity("訂單已處裡完成，無法刪除訂單。", HttpStatus.BAD_REQUEST);
        }
        if (order.getUserId() == deleteOrderDTO.getUserId()) {
            return new ResponseEntity("訂單刪除錯誤，不正確的使用者", HttpStatus.BAD_REQUEST);
        }

        orderService.customerDeleteOrder(deleteOrderDTO);

        return new ResponseEntity(HttpStatus.OK);
    }

    //進行結帳動作
    @PostMapping("/commitOrder")
    public ResponseEntity commitOrder(@RequestBody ConvertToOrderDTO convertToOrderDTO) {
        boolean response = orderService.commitOrder(convertToOrderDTO);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
