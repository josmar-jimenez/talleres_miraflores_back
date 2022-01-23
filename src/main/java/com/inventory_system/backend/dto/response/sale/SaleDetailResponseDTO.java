package com.inventory_system.backend.dto.response.sale;

import com.inventory_system.backend.dto.common.SaleDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailResponseDTO extends SaleDetailDTO {

    private Integer id;
    private Integer saleId;
    private BigDecimal price;

    private String productName;
}
