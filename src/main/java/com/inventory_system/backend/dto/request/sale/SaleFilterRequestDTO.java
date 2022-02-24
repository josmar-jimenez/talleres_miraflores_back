package com.inventory_system.backend.dto.request.sale;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleFilterRequestDTO {

    private String storeName;
    private String userName;
}
