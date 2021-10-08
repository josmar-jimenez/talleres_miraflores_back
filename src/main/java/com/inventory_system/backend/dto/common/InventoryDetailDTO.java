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
public class InventoryDetailDTO {

    @Min(0)
    @NotNull
    private Long cantPhysical;
    @Min(0)
    @NotNull
    private Long cantSystem;
    @NotNull
    private Integer productId;
}
