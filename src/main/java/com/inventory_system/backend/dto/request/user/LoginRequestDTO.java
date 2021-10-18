package com.inventory_system.backend.dto.request.user;

import com.inventory_system.backend.dto.common.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO extends UserDTO {
	
	@NotBlank
	@NotNull
	@NotEmpty
	private String nick;
	   
    @NotBlank
    @NotNull
    @NotEmpty
    private String password;
}
