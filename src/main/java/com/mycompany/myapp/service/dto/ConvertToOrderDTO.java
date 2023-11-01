package com.mycompany.myapp.service.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class ConvertToOrderDTO {

    private UUID userId;
    private String payMethod;
    private String deliveryLocation;
    private Instant deliveryTime;
    private String couponCode;
}
