package com.inventory_system.backend.service;

import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.StockMovement;
import com.inventory_system.backend.model.User;
import com.inventory_system.backend.repository.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.inventory_system.backend.util.InventorySystemConstant.*;

@Service
public class StockMovementService {

    @Autowired
    private StockMovementRepository stockMovementRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    public StockMovement findById(int id, Allowed allowed) throws BusinessException, UnauthorizedException {
        StockMovement stockMovement= stockMovementRepository.findById(id).orElseThrow(() ->
                new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND));

        if(Allowed.ALL.equals(allowed)){
            return stockMovement;
        } else {
            User userLogged = userService.findByNick(tokenService.getUserNick());
            if(!userLogged.getStore().equals(stockMovement.getSourceStore())&&
                    !userLogged.getStore().equals(stockMovement.getDestinyStore())){
                throw new BusinessException(INSUFFICIENT_PRIVILEGES_CODE, INSUFFICIENT_PRIVILEGES);
            }
            return stockMovement;
        }
    }

    public Page<StockMovement> findAll(Pageable pageable, Allowed allowed) throws UnauthorizedException {
        if(Allowed.ALL.equals(allowed)){
            return stockMovementRepository.findAll(pageable);
        } else {
            User userLogged = userService.findByNick(tokenService.getUserNick());
            return stockMovementRepository.findBySourceStoreOrDestinyStore(
                    userLogged.getStore(),userLogged.getStore(),pageable);
        }
    }

    public StockMovement create(StockMovement stockMovement, User userLogged) throws BusinessException {
        if(userLogged.getRole().getId()==1||
                userLogged.getStore().getId().equals(stockMovement.getSourceStore())||
                userLogged.getStore().getId().equals(stockMovement.getDestinyStore())) {
            return stockMovementRepository.save(stockMovement);
        }else{
        throw new BusinessException(OPERATION_NOT_ALLOWED_CODE,OPERATION_NOT_ALLOWED);
    }
    }

}
