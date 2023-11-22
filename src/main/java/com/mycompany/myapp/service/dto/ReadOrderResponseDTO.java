package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.OrderDrink;
import java.util.List;
import lombok.Data;

@Data
public class ReadOrderResponseDTO {

    private Order order;
    private List<OrderDrink> orderDrinkList;
}
