package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.common.InventoryDetailDTO;
import com.inventory_system.backend.dto.request.inventory.InventoryRequestDTO;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.enums.MovementType;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.*;
import com.inventory_system.backend.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.inventory_system.backend.util.InventorySystemConstant.*;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private UserService userService;
    @Autowired
    private StockService stockService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private StockMovementService stockMovementService;

    public Inventory findById(int id, Allowed allowed) throws BusinessException, UnauthorizedException {
        Inventory inventory= inventoryRepository.findById(id).orElseThrow(() ->
                new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND));

        if(Allowed.ALL.equals(allowed)){
            return inventory;
        } else {
            User userLogged = userService.findByNick(tokenService.getUserNick());
            if(!userLogged.getStore().equals(inventory.getStore())){
                throw new BusinessException(INSUFFICIENT_PRIVILEGES_CODE, INSUFFICIENT_PRIVILEGES);
            }
            return inventory;
        }
    }

    public Page<Inventory> findAll(Pageable pageable, Allowed allowed) throws UnauthorizedException {

        if(Allowed.ALL.equals(allowed)){
            return inventoryRepository.findAll(pageable);
        } else {
            User userLogged = userService.findByNick(tokenService.getUserNick());
            return inventoryRepository.findByStore(userLogged.getStore(),pageable);
        }
    }

    @Transactional
    public Inventory create(InventoryRequestDTO inventoryRequestDTO, Allowed allowed) throws BusinessException, UnauthorizedException {
        User userLogged = userService.findByNick(tokenService.getUserNick());

        if(Allowed.ALL.equals(allowed)||userLogged.getStore().getId().equals(inventoryRequestDTO.getStoreId())) {
            Store store = storeService.findById(inventoryRequestDTO.getStoreId());
            Inventory inventory = new Inventory(null, OffsetDateTime.now(),
                    inventoryRequestDTO.getComments(),userLogged,store,null);

            List<InventoryDetail> inventoryDetailList = new ArrayList<>();
            for(InventoryDetailDTO inventoryDetailDTO : inventoryRequestDTO.getDetail() ){
                Product product = productService.findById(inventoryDetailDTO.getProductId());
                Stock stock = stockService.findByProductIdAndStoreId(product.getId(), store.getId());
                if(Objects.isNull(stock)){
                    throw new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND);
                }
                inventoryDetailList.add(new InventoryDetail(null, inventoryDetailDTO.getCantPhysical(), stock.getStock(),
                        product,inventory));
                StockMovement stockMovement;
                if(inventoryDetailDTO.getCantPhysical()>stock.getStock()){
                    stockMovement = new StockMovement(null, MovementType.INVENTORY_EXCESS,
                            inventoryDetailDTO.getCantPhysical()-stock.getStock(),
                            userLogged,product,stock.getStore(),null);
                    stockMovementService.create(stockMovement);
                }else if(inventoryDetailDTO.getCantPhysical()<stock.getStock()){
                    stockMovement = new StockMovement(null, MovementType.INVENTORY_DEFICIT,
                            stock.getStock()-inventoryDetailDTO.getCantPhysical(),
                            userLogged,product,stock.getStore(),null);
                    stockMovementService.create(stockMovement);
                }
                stockService.updateStock(stock.getStore().getId(),product.getId(),
                        inventoryDetailDTO.getCantPhysical()-stock.getStock());
            }
            inventory.setDetail(inventoryDetailList);
            return inventoryRepository.save(inventory);
        }else{
            throw new BusinessException(OPERATION_NOT_ALLOWED_CODE,OPERATION_NOT_ALLOWED);
        }
    }
}
