package com.inventory_system.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @NotBlank
    private String shortName;
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
