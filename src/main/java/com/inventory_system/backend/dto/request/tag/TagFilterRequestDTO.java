package com.inventory_system.backend.dto.request.tag;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TagFilterRequestDTO {

    private String name;
    private String typeName;
}
