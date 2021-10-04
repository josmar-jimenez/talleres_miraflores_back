package com.inventory_system.backend.mapper;

import com.inventory_system.backend.dto.response.user.UserResponseDTO;
import com.inventory_system.backend.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserDTOMapper {

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    public void onCreate(){
        PropertyMap<User, UserResponseDTO> mapUserToUserResponseDTO = new PropertyMap<User, UserResponseDTO>() {
            @Override
            protected void configure() {
                map().setStore_id(source.getStore().getId());
                map().setRole_id(source.getRole().getId());
                map().setStatus_id(source.getStatus().getId());
            }
        };

        modelMapper.addMappings(mapUserToUserResponseDTO);
    }
}
