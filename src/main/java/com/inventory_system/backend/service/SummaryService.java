package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.response.summary.SummaryResponseDTO;
import com.inventory_system.backend.enums.SummaryTimeType;
import com.inventory_system.backend.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummaryService {

    @Autowired
    private SaleService saleService;

    @Autowired
    private StockService stockService;

    @Autowired
    private InventoryService inventoryService;

    public List<SummaryResponseDTO> findAll() throws UnauthorizedException {

        List<SummaryResponseDTO> summaryResponseDTOList = new ArrayList<>();
        long saleList;
        long inventoryList;

        /*Ventas diarias*/
        saleList = saleService.findAllFromLastType(SummaryTimeType.DAY);
        summaryResponseDTOList.add(new SummaryResponseDTO(saleList+"","Ventas diarias", "sale","bg-success"));
        /*Ventas semanales*/
        saleList = saleService.findAllFromLastType(SummaryTimeType.WEEK);
        summaryResponseDTOList.add(new SummaryResponseDTO(saleList+"","Ventas semanales", "sale","bg-info"));
        /*Ventas mensuales*/
        saleList = saleService.findAllFromLastType(SummaryTimeType.MONTH);
        summaryResponseDTOList.add(new SummaryResponseDTO(saleList+"","Ventas mensuales", "sale","bg-success"));
        /*Productos bajo stock*/
        long  stockList = stockService.findProductLowStock();
        summaryResponseDTOList.add(new SummaryResponseDTO(stockList+"","Productos con mínimo almacen", "stock","bg-danger"));

        /*Inventario con descuadre diario*/
        inventoryList = inventoryService.findAllFromLastType(SummaryTimeType.DAY);
        summaryResponseDTOList.add(new SummaryResponseDTO(inventoryList+"","Descuadres diario", "inventory","bg-success"));
        /*Inventario con descuadre semanal*/
        inventoryList = inventoryService.findAllFromLastType(SummaryTimeType.DAY);
        summaryResponseDTOList.add(new SummaryResponseDTO(inventoryList+"","Descuadres semanal", "inventory","bg-info"));
        /*Inventario con descuadre mensual*/
        inventoryList = inventoryService.findAllFromLastType(SummaryTimeType.DAY);
        summaryResponseDTOList.add(new SummaryResponseDTO(inventoryList+"","Descuadres mensuales", "inventory","bg-success"));
        return summaryResponseDTOList;
    }

}
