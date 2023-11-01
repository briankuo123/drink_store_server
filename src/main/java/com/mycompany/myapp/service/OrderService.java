package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.OrderDrink;
import com.mycompany.myapp.domain.OrderRule;
import com.mycompany.myapp.domain.ShoppingCart;
import com.mycompany.myapp.repository.*;
import com.mycompany.myapp.service.dto.ConvertToOrderDTO;
import java.time.*;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class OrderService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderDrinkRepository orderDrinkRepository;
    private final OrderRuleRepository orderRuleRepository;
    private final MenuRepository menuRepository;
    private final UserInfoRepository userInfoRepository;

    public OrderService(
        ShoppingCartRepository shoppingCartRepository,
        OrderRepository orderRepository,
        OrderDrinkRepository orderDrinkRepository,
        OrderRuleRepository orderRuleRepository,
        MenuRepository menuRepository,
        UserInfoRepository userInfoRepository
    ) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.orderDrinkRepository = orderDrinkRepository;
        this.orderRuleRepository = orderRuleRepository;
        this.menuRepository = menuRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public boolean commitOrder(ConvertToOrderDTO convertToOrderDTO) {
        Instant instantNow = Instant.now();
        //檢查訂單frequency
        if (!checkOrderFrequency(instantNow)) {
            log.error("目前訂單數量眾多請稍後嘗試");
            return false;
        }
        //checkOrderStatus
        if (!checkOrderStatus()) {
            log.error("目前店家不開放訂餐");
            return false;
        }
        //checkCoupon
        if (!checkCoupon(convertToOrderDTO.getCouponCode())) {
            log.error("無效的優惠碼");
            return false;
        }
        //checkDelivery
        if (!checkDelivery(convertToOrderDTO.getDeliveryLocation())) {
            log.error("無法提供服務的外送地址");
            return false;
        }
        //購物車轉換成訂單動作
        convertShoppingCartToOrder(convertToOrderDTO);
        return true;
    }

    public boolean checkOrderFrequency(Instant orderTime) {
        //先抓time section
        int minute = orderTime.atZone(ZoneId.of("Asia/Taipei")).getMinute();

        List<Order> orderList;

        LocalDateTime startTime;
        LocalDateTime endTime;
        //進行時間區段選取
        if (minute < 20) {
            startTime = LocalDateTime.now().minusMinutes(minute);
            endTime = LocalDateTime.now().plusMinutes(20 - minute);
        } else if (minute < 40) {
            startTime = LocalDateTime.now().minusMinutes(minute - 20);
            endTime = LocalDateTime.now().plusMinutes(40 - minute);
        } else if (minute < 60) {
            startTime = LocalDateTime.now().minusMinutes(minute - 20);
            endTime = LocalDateTime.now().plusMinutes(60 - minute);
        } else {
            startTime = LocalDateTime.now();
            endTime = LocalDateTime.now();
        }

        orderList =
            orderRepository.findByDeliveryTimeBetween(
                startTime.toInstant(ZoneOffset.ofHours(+8)),
                endTime.toInstant(ZoneOffset.ofHours(+8))
            );

        return !(orderList.size() > 3);
    }

    //檢查店家是否現在開放接單
    public boolean checkOrderStatus() {
        OrderRule orderRule = orderRuleRepository.getOrderRuleById(1L);
        return orderRule.getOrderStatus();
    }

    //檢查coupon的有效性
    public boolean checkCoupon(String couponCode) {
        return true;
    }

    //先設定成檢查地址是否在桃園市中壢區(地址轉經緯度有點麻煩)
    public boolean checkDelivery(String deliveryLocation) {
        return deliveryLocation.matches(".*桃園市中壢區.*");
    }

    public void convertShoppingCartToOrder(ConvertToOrderDTO convertToOrderDTO) {
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByUserId(convertToOrderDTO.getUserId());
        String userName = userInfoRepository.findByUserId(convertToOrderDTO.getUserId()).getUserName();
        Instant instantNow = Instant.now();
        Order order = new Order();
        UUID orderId = UUID.randomUUID();

        //create order data and save
        order.setOrderId(orderId);
        order.setUserId(convertToOrderDTO.getUserId());
        order.setPayMethod(convertToOrderDTO.getPayMethod());
        order.setDeliveryLocation(convertToOrderDTO.getDeliveryLocation());
        order.setTotalPrice(totalPrice(shoppingCartList));
        order.setDeliveryTime(convertToOrderDTO.getDeliveryTime());
        order.setOrderStatus("待處理");
        order.setCoupon(convertToOrderDTO.getCoupon());
        order.setCreateBy(userName);
        order.setCreateDatetime(instantNow);
        order.setLastModifiedBy(userName);
        order.setLastModifiedDatetime(instantNow);
        orderRepository.save(order);

        //create order drink data and save
        for (ShoppingCart sc : shoppingCartList) {
            OrderDrink orderDrink = new OrderDrink();
            orderDrink.setOrderId(orderId);
            orderDrink.setDrinkId(sc.getDrinkId());
            orderDrink.setSugar(sc.getSugar());
            orderDrink.setIce(sc.getIce());
            orderDrink.setToppings(sc.getToppings());
            orderDrink.setDrinkSize(sc.getDrinkSize());
            orderDrinkRepository.save(orderDrink);
        }
    }

    public int totalPrice(List<ShoppingCart> shoppingCartList) {
        int totalPrice = 0;
        for (ShoppingCart sc : shoppingCartList) {
            int drinkPrice = menuRepository.findByDrinkId(sc.getDrinkId()).getDrinkPrice();
            totalPrice += drinkPrice;
        }

        return totalPrice;
    }
}
