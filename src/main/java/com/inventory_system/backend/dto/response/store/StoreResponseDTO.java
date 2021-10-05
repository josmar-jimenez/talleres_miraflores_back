package com.inventory_system.backend.dto.response.store;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inventory_system.backend.dto.common.StoreDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreResponseDTO extends StoreDTO {
    private Integer id;
}
