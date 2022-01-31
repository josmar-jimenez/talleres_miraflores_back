package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.response.summary.SummaryResponseDTO;
import com.inventory_system.backend.enums.SummaryTimeType;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.Inventory;
import com.inventory_system.backend.model.Sale;
import com.inventory_system.backend.model.SaleDetail;
import com.inventory_system.backend.model.Stock;
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
        List<Sale> saleList;
        List<Inventory> inventoryList;
        /*Ventas diarias*/
        saleList = saleService.findAllFromLastType(SummaryTimeType.DAY);
        summaryResponseDTOList.add(new SummaryResponseDTO(saleList.size()+"","Ventas diarias", "sale","bg-success"));
        /*Productos vendidos diario*/
        summaryResponseDTOList.add(new SummaryResponseDTO(getTotalProducts(saleList)+"","Productos vendidos hoy",
                "sale","bg-dark"));
        /*Ventas semanales*/
        saleList = saleService.findAllFromLastType(SummaryTimeType.WEEK);
        summaryResponseDTOList.add(new SummaryResponseDTO(saleList.size()+"","Ventas semanales", "sale","bg-info"));
        /*Productos vendidos semanal*/
        summaryResponseDTOList.add(new SummaryResponseDTO(getTotalProducts(saleList)+"","Productos vendidos esta semana",
                "sale","bg-dark"));
        /*Ventas mensuales*/
        saleList = saleService.findAllFromLastType(SummaryTimeType.MONTH);
        summaryResponseDTOList.add(new SummaryResponseDTO(saleList.size()+"","Ventas mensuales", "sale","bg-success"));
        /*Productos vendidos mensual*/
        summaryResponseDTOList.add(new SummaryResponseDTO(getTotalProducts(saleList)+"","Productos vendidos este mes",
                "sale","bg-dark"));
        /*Inventario con descuadre diario*/
        inventoryList = inventoryService.findAllFromLastType(SummaryTimeType.DAY);
        summaryResponseDTOList.add(new SummaryResponseDTO(inventoryList.size()+"","Descuadres diario", "inventory","bg-info"));
        /*Inventario con descuadre semanal*/
        inventoryList = inventoryService.findAllFromLastType(SummaryTimeType.DAY);
        summaryResponseDTOList.add(new SummaryResponseDTO(inventoryList.size()+"","Descuadres semanal", "inventory","bg-danger"));
        /*Inventario con descuadre mensual*/
        inventoryList = inventoryService.findAllFromLastType(SummaryTimeType.DAY);
        summaryResponseDTOList.add(new SummaryResponseDTO(inventoryList.size()+"","Descuadres mensuales", "inventory","bg-info"));
        /*Productos bajo stock*/
        List<Stock>  stockList = stockService.findProductLowStock();
        summaryResponseDTOList.add(new SummaryResponseDTO(stockList.size()+"","Productos con mínimo almacen", "stock","bg-danger"));

        return summaryResponseDTOList;
    }

    private int getTotalProducts(List<Sale>saleList){
        int totalProducts=0;
        for(Sale sale : saleList){
            for(SaleDetail saleDetail:sale.getDetail()){
                totalProducts+=saleDetail.getCant();
            }
        }
        return totalProducts;
    }

}
