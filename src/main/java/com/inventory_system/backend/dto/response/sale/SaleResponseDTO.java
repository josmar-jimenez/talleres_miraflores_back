package com.inventory_system.backend.dto.response.sale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponseDTO {

    private Integer id;
    private BigDecimal total;
    private BigDecimal tax;
    private Integer userId;
    private Integer storeId;

    private String createdDate;
    private String createdTime;
    private String storeName;
    private String userName;

    private List<SaleDetailResponseDTO> detail;
}
