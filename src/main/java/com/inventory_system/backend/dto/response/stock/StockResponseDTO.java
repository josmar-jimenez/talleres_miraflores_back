package com.inventory_system.backend.dto.response.stock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inventory_system.backend.dto.common.StockDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockResponseDTO extends StockDTO {
    private Integer id;
}
