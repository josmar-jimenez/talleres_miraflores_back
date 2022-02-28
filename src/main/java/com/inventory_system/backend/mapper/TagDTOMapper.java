package com.inventory_system.backend.mapper;

import com.inventory_system.backend.dto.response.tag.TagResponseDTO;
import com.inventory_system.backend.model.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TagDTOMapper {

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    public void onCreate() {
        PropertyMap<Tag, TagResponseDTO> mapStoreToStoreResponseDTO = new PropertyMap<Tag, TagResponseDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setName(source.getName());
                map().setTypeName(source.getType().getName());
                map().setTypeId(source.getType().getId());
            }
        };

        modelMapper.addMappings(mapStoreToStoreResponseDTO);
    }
}
