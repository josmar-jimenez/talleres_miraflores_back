package com.inventory_system.backend.dto.request.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagRequestDTO {

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$")
    private String name;

    private Integer fatherId;
    @NotNull
    private Integer typeId;
}
