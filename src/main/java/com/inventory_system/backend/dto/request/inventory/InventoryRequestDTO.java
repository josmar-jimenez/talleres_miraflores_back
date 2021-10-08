package com.inventory_system.backend.dto.request.inventory;

import com.inventory_system.backend.dto.common.InventoryDetailDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InventoryRequestDTO {

    @NotNull
    Integer storeId;
    String comments;
    List<InventoryDetailDTO> detail;
}
