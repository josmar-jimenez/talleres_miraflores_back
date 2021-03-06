package com.inventory_system.backend.dto.response.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDTO {

    private Integer id;
    private Integer userId;
    private Integer storeId;
    private String createdDate;
    private String createdTime;
    private String comments;
    private String storeName;
    private String userName;
    private boolean hasMismatch;
    private List<InventoryDetailResponseDTO> detail;
}
