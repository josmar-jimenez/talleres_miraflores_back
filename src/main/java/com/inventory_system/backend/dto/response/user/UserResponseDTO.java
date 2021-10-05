package com.inventory_system.backend.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inventory_system.backend.dto.common.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO extends UserDTO {

    private Integer id;
}
