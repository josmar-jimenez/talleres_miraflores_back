package com.inventory_system.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {

    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String address;
    @Email
    private String email;
    private String phone;
    private String image;
    /*Relations*/
    @NotNull
    private int status_id;

}
