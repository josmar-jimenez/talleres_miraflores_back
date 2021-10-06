package com.inventory_system.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotBlank
    @NotNull
    @NotEmpty
    private String nick;
    @NotEmpty
    private String name;
    @NotBlank
    @NotEmpty
    private String cellphone;
    private String address;
    @Email
    private String email;
    private String emergencyPhone;
    private String emergencyContact;
    /*Relations*/
    @NotNull
    private int storeId;
    @NotNull
    private int statusId;
    @NotNull
    private int roleId;
}
