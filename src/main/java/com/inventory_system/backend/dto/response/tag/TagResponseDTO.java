package com.inventory_system.backend.dto.response.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagResponseDTO {

    private Integer id;
    private Integer fatherId;
    private Integer typeId;
    private String name;
    private String typeName;
    private String fatherName;
    private String fatherTypeName;
}
