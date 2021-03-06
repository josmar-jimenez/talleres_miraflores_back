package com.inventory_system.backend.mapper;

import com.inventory_system.backend.dto.response.store.StoreResponseDTO;
import com.inventory_system.backend.model.Store;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StoreDTOMapper {

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    public void onCreate() {
        PropertyMap<Store, StoreResponseDTO> mapStoreToStoreResponseDTO = new PropertyMap<Store, StoreResponseDTO>() {
            @Override
            protected void configure() {
                map().setStatusId(source.getStatus().getId());
                map().setStatusName(source.getStatus().getName());
            }
        };

        modelMapper.addMappings(mapStoreToStoreResponseDTO);
    }
}
