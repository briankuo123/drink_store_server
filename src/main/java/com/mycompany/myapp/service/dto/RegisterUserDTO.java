package com.mycompany.myapp.service.dto;

import lombok.Data;

@Data
public class RegisterUserDTO {

    private String login;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String createBy;
}
