package com.inventory_system.backend.mapper;

import com.inventory_system.backend.dto.response.user.RoleOperativeActionResponseDTO;
import com.inventory_system.backend.model.RoleAction;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RoleActionMapper {

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    public void onCreate() {
        PropertyMap<RoleAction, RoleOperativeActionResponseDTO> mapRoleActionToRoleOperativeActionResponseDTO
                = new PropertyMap<RoleAction, RoleOperativeActionResponseDTO>() {
            @Override
            protected void configure() {
                map().setAction_name(source.getAction().getName());
                map().setOperative_name(source.getOperative().getName());
            }
        };

        modelMapper.addMappings(mapRoleActionToRoleOperativeActionResponseDTO);
    }
}
