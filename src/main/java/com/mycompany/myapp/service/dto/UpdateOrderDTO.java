package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.OrderDrink;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class UpdateOrderDTO {

    private UUID orderId;
    private UUID userId;
    private String payMethod;
    private String deliveryLocation;
    private Instant deliveryTime;
    private Boolean orderDrinkListChanged;
    private List<OrderDrink> orderDrinkList;
}
