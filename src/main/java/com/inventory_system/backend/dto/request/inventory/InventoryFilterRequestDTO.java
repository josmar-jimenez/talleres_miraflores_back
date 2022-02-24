package com.inventory_system.backend.dto.request.inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InventoryFilterRequestDTO {

    private String storeName;
}
