package com.inventory_system.backend.mapper;

import com.inventory_system.backend.dto.response.inventory.InventoryDetailResponseDTO;
import com.inventory_system.backend.dto.response.inventory.InventoryResponseDTO;
import com.inventory_system.backend.model.Inventory;
import com.inventory_system.backend.model.InventoryDetail;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static com.inventory_system.backend.util.InventorySystemConstant.DATE_FORMATTER;
import static com.inventory_system.backend.util.InventorySystemConstant.TIME_FORMATTER;

@Component
public class InventoryDTOMapper {

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    public void onCreate() {
        PropertyMap<Inventory, InventoryResponseDTO> mapInventoryToInventoryResponseDTO = new PropertyMap<Inventory, InventoryResponseDTO>() {
            @Override
            protected void configure() {
                map().setStoreId(source.getStore().getId());
                map().setStoreName(source.getStore().getName());
                map().setUserName(source.getUser().getName());
                map().setUserId(source.getUser().getId());
                map().setHasMismatch(source.isHasMismatch());
                using(dateToStringConverter).map(source.getCreated()).setCreatedDate(null);
                using(timeToStringConverter).map(source.getCreated()).setCreatedTime(null);
            }
        };

        PropertyMap<InventoryDetail, InventoryDetailResponseDTO> mapInventoryDetailToInventoryDetailResponseDTO
                = new PropertyMap<InventoryDetail, InventoryDetailResponseDTO>() {
            @Override
            protected void configure() {
                map().setProductId(source.getProduct().getId());
                map().setProductName(source.getProduct().getName());
            }
        };

        modelMapper.addMappings(mapInventoryToInventoryResponseDTO);
        modelMapper.addMappings(mapInventoryDetailToInventoryDetailResponseDTO);
    }

    Converter<OffsetDateTime, String> dateToStringConverter = context ->
            (context.getSource() != null ? DateTimeFormatter.ofPattern(DATE_FORMATTER).format(context.getSource()) : "");

    Converter<OffsetDateTime, String> timeToStringConverter = context ->
            (context.getSource() != null ? DateTimeFormatter.ofPattern(TIME_FORMATTER).format(context.getSource()) : "");
}
