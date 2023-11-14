package com.mycompany.myapp.service.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class CouponDTO {

    private String couponCode;
    private Integer couponValue;
    private Integer couponUseTimes;
    private Instant couponExpireDateTime;
}
