package com.inventory_system.backend.dto.request.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductFilterRequestDTO {

    private String name;
    private String code;
}
