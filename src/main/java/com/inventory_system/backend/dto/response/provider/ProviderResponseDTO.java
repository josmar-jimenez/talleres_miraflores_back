package com.inventory_system.backend.dto.response.provider;

import com.inventory_system.backend.dto.common.ProviderDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProviderResponseDTO extends ProviderDTO {
    private Integer id;
    private String statusName;
}
