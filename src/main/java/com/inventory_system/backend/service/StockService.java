package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.request.stock.StockRequestDTO;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.Stock;
import com.inventory_system.backend.model.User;
import com.inventory_system.backend.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.inventory_system.backend.util.InventorySystemConstant.*;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    public Stock findByProductIdAndStoreId(int productId, int storeId) {
        return stockRepository.findByProductIdAndStoreId(productId,storeId).orElse(null);
    }

    public Stock findById(int id, Allowed allowed) throws BusinessException, UnauthorizedException {
        Stock stock= stockRepository.findById(id).orElseThrow(() ->
                new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND));

        if(Allowed.ALL.equals(allowed)){
            return stock;
        } else {
            User userLogged = userService.findByNick(tokenService.getUserNick());
            if(!userLogged.getStore().equals(stock.getStore())){
                throw new BusinessException(INSUFFICIENT_PRIVILEGES_CODE, INSUFFICIENT_PRIVILEGES);
            }
            return stock;
        }
    }

    public Page<Stock> findAll(Pageable pageable, Allowed allowed) throws UnauthorizedException {

        if(Allowed.ALL.equals(allowed)){
            return stockRepository.findAll(pageable);
        } else {
            User userLogged = userService.findByNick(tokenService.getUserNick());
            return stockRepository.findByStore(userLogged.getStore(),pageable);
        }
    }

    public Stock create(StockRequestDTO stockRequestDTO, Allowed allowed) throws BusinessException, UnauthorizedException {
        Stock stockExists = findByProductIdAndStoreId(stockRequestDTO.getProductId(),stockRequestDTO.getStoreId());
        if( Objects.isNull(stockExists)){
            User userLogged = userService.findByNick(tokenService.getUserNick());
            if(Allowed.ALL.equals(allowed)||userLogged.getStore().getId().equals(stockRequestDTO.getStoreId())) {
                Stock stock = new Stock(null,stockRequestDTO.getStock(),null,null,null);
                stock.setProduct(productService.findById(stockRequestDTO.getProductId()));
                stock.setStore(storeService.findById(stockRequestDTO.getStoreId()));
                stock.setStatus(statusService.findById(stockRequestDTO.getStatusId()));
                return stockRepository.save(stock);
                /*TODO: INVOCAR MOVIMIENTO DE INVENTARIO*/
            }else{
                throw new BusinessException(OPERATION_NOT_ALLOWED_CODE,OPERATION_NOT_ALLOWED);
            }
        }else{
            throw  new BusinessException(RECORD_EXIST_CODE,RECORD_EXIST+"productId, storeId");
        }
    }

    public Stock update(StockRequestDTO stockRequestDTO, int id, Allowed allowed) throws BusinessException, UnauthorizedException {
        Stock stock   = findById(id, allowed);
        User userLogged = userService.findByNick(tokenService.getUserNick());

        if(Allowed.ALL.equals(allowed)||stock.getStore().equals(userLogged.getStore())) {
            stock.setStock(stockRequestDTO.getStock());
            stock.setStatus(statusService.findById(stockRequestDTO.getStatusId()));
            stock = stockRepository.save(stock);
            /*TODO: INVOCAR MOVIMIENTO DE INVENTARIO*/
        }else{
            throw new BusinessException(OPERATION_NOT_ALLOWED_CODE,OPERATION_NOT_ALLOWED);
        }
        return stock;
    }

    public boolean delete(int id, Allowed allowed) throws BusinessException, UnauthorizedException {
        Stock stockToDelete = findById(id,allowed);
        stockRepository.delete(stockToDelete);
        /*TODO: INVOCAR MOVIMIENTO DE INVENTARIO*/
        return true;
    }

    public void updateStock(Integer storeId, Integer productId, Integer cant) throws BusinessException {

        Stock stockExists = findByProductIdAndStoreId(productId,storeId);
        stockExists.setStock(stockExists.getStock()+cant);
        /*Status agotado*/
        if(stockExists.getStock()==0)
            stockExists.setStatus(statusService.findById(5));
        else if(stockExists.getStock()<=0)
            throw new BusinessException(INVALID_SALE_REQUEST_INSUFFICIENT_STOCK_CODE,
                    INVALID_SALE_REQUEST_INSUFFICIENT_STOCK);
        stockRepository.save(stockExists);
        /*TODO: INVOCAR MOVIMIENTO DE INVENTARIO*/
    }
}
