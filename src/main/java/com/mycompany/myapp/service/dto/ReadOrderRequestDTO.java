package com.mycompany.myapp.service.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class ReadOrderRequestDTO {

    private UUID userId;
    private String orderStatus;
}
