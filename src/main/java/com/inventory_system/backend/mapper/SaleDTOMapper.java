package com.inventory_system.backend.mapper;

import com.inventory_system.backend.dto.response.sale.SaleDetailResponseDTO;
import com.inventory_system.backend.dto.response.sale.SaleResponseDTO;
import com.inventory_system.backend.model.Sale;
import com.inventory_system.backend.model.SaleDetail;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.inventory_system.backend.util.InventorySystemConstant.DATE_FORMATTER;
import static com.inventory_system.backend.util.InventorySystemConstant.TIME_FORMATTER;

@Component
public class SaleDTOMapper {

    @Autowired
    ModelMapper modelMapper;
    private ZoneId timeZoneActual = ZoneId.of("America/Caracas");

    @PostConstruct
    public void onCreate() {
        PropertyMap<Sale, SaleResponseDTO> mapSaleToSaleResponseDTO = new PropertyMap<Sale, SaleResponseDTO>() {
            @Override
            protected void configure() {
                map().setStoreId(source.getStore().getId());
                map().setStoreName(source.getStore().getName());
                map().setUserId(source.getUser().getId());
                map().setUserName(source.getUser().getName());
                using(dateToStringConverter).map(source.getCreated()).setCreatedDate(null);
                using(timeToStringConverter).map(source.getCreated()).setCreatedTime(null);
            }
        };

        PropertyMap<SaleDetail, SaleDetailResponseDTO> mapSaleDetailToSaleDetailResponseDTO
                = new PropertyMap<SaleDetail, SaleDetailResponseDTO>() {
            @Override
            protected void configure() {
                map().setProductId(source.getProduct().getId());
                map().setProductName(source.getProduct().getName());
                map().setPrice(source.getPrice());

            }
        };

        modelMapper.addMappings(mapSaleToSaleResponseDTO);
        modelMapper.addMappings(mapSaleDetailToSaleDetailResponseDTO);
    }

    Converter<OffsetDateTime, String> dateToStringConverter = context ->
            (context.getSource() != null ? DateTimeFormatter.ofPattern(DATE_FORMATTER).format(context.getSource().atZoneSameInstant(timeZoneActual)) : "");

    Converter<OffsetDateTime, String> timeToStringConverter = context ->
            (context.getSource() != null ? DateTimeFormatter.ofPattern(TIME_FORMATTER).format(context.getSource().atZoneSameInstant(timeZoneActual)) : "");
}
