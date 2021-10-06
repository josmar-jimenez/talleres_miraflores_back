package com.inventory_system.backend.mapper;

import com.inventory_system.backend.dto.request.provider.ProviderRequestDTO;
import com.inventory_system.backend.model.Provider;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ProviderDTOMapper {

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    public void onCreate(){
        PropertyMap<Provider, ProviderRequestDTO> mapProviderToProviderResponseDTO = new PropertyMap<Provider, ProviderRequestDTO>() {
            @Override
            protected void configure() {
                map().setStatusId(source.getStatus().getId());
            }
        };

        modelMapper.addMappings(mapProviderToProviderResponseDTO);
    }
}
