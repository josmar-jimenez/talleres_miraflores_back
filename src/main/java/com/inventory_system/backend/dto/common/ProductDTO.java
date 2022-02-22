package com.inventory_system.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @NotNull
    @NotEmpty
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$")
    private String shortName;
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$")
    private String barcode;
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$")
    private String code;
    private String color;
    private String manufacturer;
    @NotNull
    private BigDecimal price;
    @NotNull
    private BigDecimal cost;
    private BigDecimal tax;
    /*Relations*/
    @NotNull
    private int statusId;

}
