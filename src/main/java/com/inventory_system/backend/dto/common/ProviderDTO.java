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
public class ProviderDTO {

    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$")
    private String shortName;
    @NotNull
    @NotEmpty
    private String address;
    @Email
    private String email;
    @Pattern(regexp = "^[0-9]*$")
    private String phone;
    /*Relations*/
    @NotNull
    private int statusId;

}
