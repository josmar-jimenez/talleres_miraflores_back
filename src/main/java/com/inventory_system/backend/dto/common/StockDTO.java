package com.inventory_system.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockDTO {

    @Min(0)
    @NotNull
    private Long stock;
    /*Relations*/
    @NotNull
    private Integer statusId;
    @NotNull
    private Integer productId;
    @NotNull
    private Integer storeId;
}
