package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.response.summary.SummaryResponseDTO;
import com.inventory_system.backend.enums.SummaryTimeType;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.Sale;
import com.inventory_system.backend.repository.TagRepository;
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

    public List<SummaryResponseDTO> findAll() throws UnauthorizedException {

        List<SummaryResponseDTO> summaryResponseDTOList = new ArrayList<>();
        List<Sale> saleList;
        /*Ventas diarias*/
        saleList = saleService.findAllFromLastType(SummaryTimeType.DAY);
        summaryResponseDTOList.add(new SummaryResponseDTO(saleList.size()+"","Ventas diarias", "sale","bg-success"));
        /*Ventas semanales*/
        saleList = saleService.findAllFromLastType(SummaryTimeType.WEEK);
        summaryResponseDTOList.add(new SummaryResponseDTO(saleList.size()+"","Ventas semanales", "sale","bg-info"));
        /*Ventas mensuales*/
        saleList = saleService.findAllFromLastType(SummaryTimeType.MONTH);
        summaryResponseDTOList.add(new SummaryResponseDTO(saleList.size()+"","Ventas mensuales", "sale","bg-warning"));
        /*Productos vendidos diario*/
        /*Productos vendidos semanal*/
        /*Productos vendidos mensual*/

        /*Inventario con descuadre semanal*/
        /*Productos bajo stock*/
        return summaryResponseDTOList;
    }

}
