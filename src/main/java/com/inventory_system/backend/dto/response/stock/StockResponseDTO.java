package com.inventory_system.backend.dto.response.stock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inventory_system.backend.dto.common.StockDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockResponseDTO extends StockDTO {
    private Integer id;
    private BigDecimal productPrice;
    @NotNull
    private String productName;
    @NotNull
    private String storeName;
    @NotNull
    private String statusName;
}
