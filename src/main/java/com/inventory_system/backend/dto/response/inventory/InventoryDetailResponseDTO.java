package com.inventory_system.backend.dto.response.inventory;

import com.inventory_system.backend.dto.common.InventoryDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDetailResponseDTO extends InventoryDetailDTO {

    private Integer id;
}
