package com.mycompany.myapp.service.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class UserDataDTO {

    private UUID userId;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userPassword;
}
