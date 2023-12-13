package com.mycompany.myapp.service.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {

    private String login;
    private String newLogin;
    private String password;
    private String newPassword;
    private String name;
    private String email;
    private Boolean passwordChanged;
}
