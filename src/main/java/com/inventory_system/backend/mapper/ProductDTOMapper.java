package com.inventory_system.backend.mapper;

import com.inventory_system.backend.dto.response.product.ProductResponseDTO;
import com.inventory_system.backend.model.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ProductDTOMapper {

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    public void onCreate(){
        PropertyMap<Product, ProductResponseDTO> mapProductToProductResponseDTO = new PropertyMap<Product, ProductResponseDTO>() {
            @Override
            protected void configure() {
                map().setStatusId(source.getStatus().getId());
                map().setStatusName(source.getStatus().getName());
            }
        };

        modelMapper.addMappings(mapProductToProductResponseDTO);
    }
}
