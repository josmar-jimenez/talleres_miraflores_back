package com.inventory_system.backend.dto.request.stock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockFilterRequestDTO {

    private String storeName;
    private String productName;
}
