package com.mycompany.myapp.service.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class DeleteOrderDTO {

    private UUID orderId;
    private UUID userId;
}
