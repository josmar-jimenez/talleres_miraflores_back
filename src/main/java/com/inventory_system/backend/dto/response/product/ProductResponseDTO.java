package com.inventory_system.backend.dto.response.product;

import com.inventory_system.backend.dto.common.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO extends ProductDTO {
    private Integer id;
}
