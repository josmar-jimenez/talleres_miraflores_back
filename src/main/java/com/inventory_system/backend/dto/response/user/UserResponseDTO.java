package com.inventory_system.backend.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Integer id;
    private String nick;
    private String name;
    private String cellphone;
    private String address;
    private String email;
    private String emergencyPhone;
    private String emergencyContact;
    /*Relations*/
    private int store_id;
    private int status_id;
    private int role_id;
}
