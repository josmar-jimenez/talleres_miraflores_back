package com.inventory_system.backend.dto.request.sale;

import com.inventory_system.backend.dto.common.SaleDetailDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SaleRequestDTO{

    @NotNull
    Integer storeId;
    List<SaleDetailDTO> detail;
}
