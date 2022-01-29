package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.common.SaleDetailDTO;
import com.inventory_system.backend.dto.request.sale.SaleRequestDTO;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.enums.MovementType;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.*;
import com.inventory_system.backend.repository.SaleRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.inventory_system.backend.util.InventorySystemConstant.*;

@Service
@Slf4j
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StockMovementService stockMovementService;

    public Sale findById(int id, Allowed allowed) throws BusinessException, UnauthorizedException {
        Sale sale = saleRepository.findById(id).orElseThrow(()
                -> new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND));

        if (Allowed.ALL.equals(allowed)) {
            return sale;
        } else {
            User userLogged = userService.findByNick(tokenService.getUserNick());
            if (!userLogged.getStore().equals(sale.getStore())) {
                throw new BusinessException(INSUFFICIENT_PRIVILEGES_CODE, INSUFFICIENT_PRIVILEGES);
            }
            return sale;
        }
    }

    public Page<Sale> findAll(Pageable pageable, Allowed allowed) throws UnauthorizedException {
        if(pageable.getSort().isEmpty()) {
            Sort sortDefault = Sort.by("created").descending();
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortDefault);
        }
        if (Allowed.ALL.equals(allowed)) {
            return saleRepository.findAll(pageable);
        } else {
            User userLogged = userService.findByNick(tokenService.getUserNick());
            return saleRepository.findByStore(userLogged.getStore(), pageable);
        }
    }

    @Transactional
    public Sale create(SaleRequestDTO saleRequestDTO, Allowed allowed) throws BusinessException, UnauthorizedException {

        if (CollectionUtils.isEmpty(saleRequestDTO.getDetail())) {
            throw new BusinessException(INVALID_SALE_REQUEST_NO_PRODUCT_CODE, INVALID_SALE_REQUEST_NO_PRODUCT);
        }

        User userLogged = userService.findByNick(tokenService.getUserNick());

        Sale sale = new Sale();
        List<SaleDetail> saleDetailList = new ArrayList<>();
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;

        for (SaleDetailDTO saleDetailDTO : saleRequestDTO.getDetail()) {
            Product product = productService.findById(saleDetailDTO.getProductId());
            Stock stock = stockService.findByProductIdAndStoreId(product.getId(), saleRequestDTO.getStoreId());
            if (Objects.isNull(stock) || stock.getStock() < saleDetailDTO.getCant()) {
                throw new BusinessException(INVALID_SALE_REQUEST_INSUFFICIENT_STOCK_CODE,
                        INVALID_SALE_REQUEST_INSUFFICIENT_STOCK);
            }
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(saleDetailDTO.getCant())));
            totalCost = totalCost.add(product.getCost().multiply(BigDecimal.valueOf(saleDetailDTO.getCant())));
            /*TODO: Sacar tax de variable de configuración de base de datos*/
            tax = tax.add(BigDecimal.valueOf(saleDetailDTO.getCant()).multiply(BigDecimal.TEN));

            saleDetailList.add(new SaleDetail(null, saleDetailDTO.getCant(),
                    product.getPrice(), product.getCost(), null, product, sale));
        }

        Store store;
        if (Allowed.ALL.equals(allowed)) {
            store = storeService.findById(saleRequestDTO.getStoreId());
        } else {
            store = userLogged.getStore();
        }

        sale.setStore(store);
        sale.setTax(tax);
        sale.setTotalCost(totalCost);
        sale.setTotal(total);
        sale.setDetail(saleDetailList);
        sale.setUser(userLogged);
        sale.setCreated(OffsetDateTime.now());
        sale = saleRepository.save(sale);

        for (SaleDetail saleDetail : sale.getDetail()) {
            StockMovement stockMovement = new StockMovement(null, MovementType.SALE,
                    (long) saleDetail.getCant(), userLogged,
                    saleDetail.getProduct(), store, null);
            stockMovementService.create(stockMovement);
            stockService.updateStock(store.getId(), saleDetail.getProduct().getId(), saleDetail.getCant() * -1L);
        }
        return sale;
    }
}
