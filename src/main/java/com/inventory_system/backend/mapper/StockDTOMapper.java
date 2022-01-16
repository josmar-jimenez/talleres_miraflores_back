package com.inventory_system.backend.mapper;

import com.inventory_system.backend.dto.response.stock.StockResponseDTO;
import com.inventory_system.backend.model.Stock;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StockDTOMapper {

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    public void onCreate(){
        PropertyMap<Stock, StockResponseDTO> mapStockToStockResponseDTO = new PropertyMap<Stock, StockResponseDTO>() {
            @Override
            protected void configure() {
                map().setStatusId(source.getStatus().getId());
                map().setProductId(source.getProduct().getId());
                map().setStoreId(source.getStore().getId());
                map().setProductName(source.getProduct().getName());
                map().setStoreName(source.getStore().getName());
                map().setStatusName(source.getStatus().getName());
            }
        };

        modelMapper.addMappings(mapStockToStockResponseDTO);
    }
}
