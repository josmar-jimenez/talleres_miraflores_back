package com.inventory_system.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
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
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    private String shortName;
    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    private String barcode;
    @NotNull
    private BigDecimal price;
    @NotNull
    private BigDecimal cost;
    private String image;
    /*Relations*/
    @NotNull
    private int statusId;

}
