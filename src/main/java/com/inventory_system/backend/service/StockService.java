package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.request.stock.StockFilterRequestDTO;
import com.inventory_system.backend.dto.request.stock.StockRequestDTO;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.enums.MovementType;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.*;
import com.inventory_system.backend.repository.ProductRepository;
import com.inventory_system.backend.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;
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
    @Autowired
    private StockMovementService stockMovementService;
    @Autowired
    private ElasticSearchService elasticSearchService;

    public Stock findByProductIdAndStoreId(int productId, int storeId) {
        return stockRepository.findByProductIdAndStoreId(productId, storeId).orElse(null);
    }

    public List<Stock> findByProductId(int productId) {
        return stockRepository.findByProductId(productId);
    }

    public Stock findById(int id, Allowed allowed) throws BusinessException, UnauthorizedException {
        Stock stock = stockRepository.findById(id).orElseThrow(() ->
                new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND));
/*        if (Allowed.ALL.equals(allowed)) {
            return stock;
        } else {
            User userLogged = userService.findByNick(tokenService.getUserNick());
            if (!userLogged.getStore().equals(stock.getStore())) {
                throw new BusinessException(INSUFFICIENT_PRIVILEGES_CODE, INSUFFICIENT_PRIVILEGES);
            }
            return stock;
        }*/
        return stock;
    }

    public Page<Stock> findAll(Pageable pageable, Allowed allowed) throws UnauthorizedException {
        if (pageable.getSort().isEmpty()) {
            Sort sortDefault = Sort.by("id").descending();
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortDefault);
        }
        return stockRepository.findAll(pageable);
/*        if (Allowed.ALL.equals(allowed)) {
            return stockRepository.findAll(pageable);
        } else {
            User userLogged = userService.findByNick(tokenService.getUserNick());
            return stockRepository.findByStore(userLogged.getStore(), pageable);
        }*/
    }

    public Page<Stock> findAllFiltered(Pageable pageable, StockFilterRequestDTO stockFilterRequestDTO) throws UnauthorizedException {
        if (pageable.getSort().isEmpty()) {
            Sort sortDefault = Sort.by("id").descending();
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortDefault);
        }

        if (ObjectUtils.isEmpty(stockFilterRequestDTO.getProductName()) && ObjectUtils.isEmpty(stockFilterRequestDTO.getStoreName())) {
            return findAll(pageable,null);
        } else if (ObjectUtils.isEmpty(stockFilterRequestDTO.getProductName()) || ObjectUtils.isEmpty(stockFilterRequestDTO.getStoreName())) {
            return stockRepository.findByProductNameContainingIgnoreCaseOrStoreNameContainingIgnoreCase(
                    ObjectUtils.isEmpty(stockFilterRequestDTO.getProductName()) ? "ÑÑÑ" : stockFilterRequestDTO.getProductName().toLowerCase(),
                    ObjectUtils.isEmpty(stockFilterRequestDTO.getStoreName()) ? "ÑÑÑ" : stockFilterRequestDTO.getStoreName().toLowerCase(), pageable);
        } else {
            return stockRepository.findByProductNameContainingIgnoreCaseAndStoreNameContainingIgnoreCase(
                    stockFilterRequestDTO.getProductName().toLowerCase(), stockFilterRequestDTO.getStoreName().toLowerCase(), pageable);
        }
    }

    public List<Stock> findProductLowStock() throws UnauthorizedException {

        User userLogged = userService.findByNick(tokenService.getUserNick());
        if (userLogged.getRole().getId() == 1) {
            return stockRepository.findByStockLessThan(10l);
        } else {
            return stockRepository.findByStoreAndStockLessThan(userLogged.getStore(), 10l);
        }
    }

    @Transactional
    public Stock create(StockRequestDTO stockRequestDTO, Allowed allowed) throws BusinessException, UnauthorizedException {
        Stock stockExists = findByProductIdAndStoreId(stockRequestDTO.getProductId(), stockRequestDTO.getStoreId());
        if (Objects.isNull(stockExists)) {
            User userLogged = userService.findByNick(tokenService.getUserNick());
            if (Allowed.ALL.equals(allowed) || userLogged.getStore().getId().equals(stockRequestDTO.getStoreId())) {

                Product product = productService.findById(stockRequestDTO.getProductId());
                Store store = storeService.findById(stockRequestDTO.getStoreId());
                Stock stock = new Stock(null, stockRequestDTO.getStock(), null, null, null);
                stock.setProduct(product);
                stock.setStore(store);
                stock.setStatus(statusService.findById(stockRequestDTO.getStatusId()));

                StockMovement stockMovement = new StockMovement(null, MovementType.STOCK_MODIFICATION,
                        stockRequestDTO.getStock(), userLogged, product, store, null);
                stockMovementService.create(stockMovement);

                if (stock.getStock() > 0 && stock.getStatus().getId() == 1 &&
                        stock.getStore().getStatus().getId() == 1
                        && stock.getProduct().getStatus().getId() == 1)
                    elasticSearchService.insertUpdateDocument(stock.getProduct());
                return stockRepository.save(stock);
            } else {
                throw new BusinessException(OPERATION_NOT_ALLOWED_CODE, OPERATION_NOT_ALLOWED);
            }
        } else {
            throw new BusinessException(RECORD_EXIST_CODE, RECORD_EXIST + "productId, storeId");
        }
    }

    @Transactional
    public Stock update(StockRequestDTO stockRequestDTO, int id, Allowed allowed) throws BusinessException, UnauthorizedException {

        Stock stock = findById(id, allowed);
        User userLogged = userService.findByNick(tokenService.getUserNick());

        if (Allowed.ALL.equals(allowed) || stock.getStore().equals(userLogged.getStore())) {
            StockMovement stockMovement = new StockMovement(null, MovementType.STOCK_MODIFICATION,
                    stockRequestDTO.getStock() - stock.getStock(), userLogged,
                    stock.getProduct(), stock.getStore(), null);
            stockMovementService.create(stockMovement);

            stock.setStock(stockRequestDTO.getStock());
            if (stockRequestDTO.getStock() < 1) {
                /*Status agotado*/
                stock.setStatus(statusService.findById(5));
                stock = stockRepository.save(stock);
                if (findByProductId(stock.getProduct().getId()).size() == 0) {
                    elasticSearchService.deleteDocument(stock.getProduct());
                }
            } else {
                Status status = statusService.findById(stockRequestDTO.getStatusId());
                stock.setStatus(status);
                stock = stockRepository.save(stock);

                if (findByProductId(stock.getProduct().getId()).size() == 0) {
                    elasticSearchService.deleteDocument(stock.getProduct());
                } else {
                    elasticSearchService.insertUpdateDocument(stock.getProduct());
                }
            }
        } else {
            throw new BusinessException(OPERATION_NOT_ALLOWED_CODE, OPERATION_NOT_ALLOWED);
        }
        return stock;
    }

    @Transactional
    public boolean delete(int id, Allowed allowed) throws BusinessException, UnauthorizedException {
        User userLogged = userService.findByNick(tokenService.getUserNick());
        Stock stockToDelete = findById(id, allowed);
        StockMovement stockMovement = new StockMovement(null, MovementType.STOCK_MODIFICATION,
                -stockToDelete.getStock(), userLogged,
                stockToDelete.getProduct(), stockToDelete.getStore(), null);
        stockMovementService.create(stockMovement);
        stockRepository.delete(stockToDelete);
        if (findByProductId(stockToDelete.getProduct().getId()).size() == 0) {
            elasticSearchService.deleteDocument(stockToDelete.getProduct());
        }
        return true;
    }

    @Transactional
    public void updateStock(Integer storeId, Integer productId, Long cant) throws BusinessException {

        Stock stockExists = findByProductIdAndStoreId(productId, storeId);

        stockExists.setStock(stockExists.getStock() + cant);
        /*Status agotado*/
        if (stockExists.getStock() == 0) {
            stockExists.setStatus(statusService.findById(5));
            stockRepository.save(stockExists);
            if (findByProductId(stockExists.getProduct().getId()).size() == 0) {
                elasticSearchService.deleteDocument(stockExists.getProduct());
            } else {
                elasticSearchService.insertUpdateDocument(stockExists.getProduct());
            }
        } else {
            if (stockExists.getStock() <= 0)
                throw new BusinessException(INVALID_SALE_REQUEST_INSUFFICIENT_STOCK_CODE,
                        INVALID_SALE_REQUEST_INSUFFICIENT_STOCK);
            stockRepository.save(stockExists);
        }
    }

    //BORRAR CREAR INVENTARIO FAKE
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public boolean createFake() throws BusinessException, UnauthorizedException {

        List<Product> product = productRepository.findAll();
        Status status = statusService.findById(1);
        Store store = storeService.findById(1);

        int i = 0;
        for (; i < product.size(); i++) {
            if (i == 500)
                break;
            try {
                Stock stock = new Stock(null, 1l, status, product.get(i), store);
               elasticSearchService.insertUpdateDocument(stock.getProduct());
                //stockRepository.save(stock);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }
}
