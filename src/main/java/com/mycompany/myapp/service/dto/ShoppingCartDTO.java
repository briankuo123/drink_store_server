package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.ShoppingCart;
import lombok.Data;

@Data
public class ShoppingCartDTO {

    private String drinkName;
    private Integer drinkPrice;
    private ShoppingCart shoppingCart;
}
