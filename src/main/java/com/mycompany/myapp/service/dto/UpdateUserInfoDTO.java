package com.mycompany.myapp.service.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class UpdateUserInfoDTO {

    private UUID userId;
    private String userName;
    private String email;
    private String phone;
}
