package com.inventory_system.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$")
    private String nick;
    @NotEmpty
    private String name;
    @NotEmpty
    @Pattern(regexp = "^[0-9]*$")
    private String cellphone;
    private String address;
    @Email
    private String email;
    @Pattern(regexp = "^[0-9]*$")
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
