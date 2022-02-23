package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.request.store.StoreRequestDTO;
import com.inventory_system.backend.dto.response.store.StoreResponseDTO;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.Stock;
import com.inventory_system.backend.model.Store;
import com.inventory_system.backend.model.User;
import com.inventory_system.backend.repository.StoreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.inventory_system.backend.util.InventorySystemConstant.*;
import static com.inventory_system.backend.util.InventorySystemConstant.RECORD_EXIST;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    TokenService tokenService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private UserService userService;
    @Autowired
    private StockService stockService;
    @Autowired
    private ModelMapper modelMapper;

    public Store findById(int id) throws BusinessException {
        return storeRepository.findById(id).orElseThrow(()
                -> new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND));
    }

    public List<StoreResponseDTO> findByProductId(int id) {
        List<Stock> stockList = stockService.findByProductId(id);
        List<StoreResponseDTO> storeResponseDTOList = new ArrayList<>();
        stockList.forEach(stock -> {
            if (stock.getStore().getStatus().getId() == 1)
                storeResponseDTOList.add(modelMapper.map(stock.getStore(), StoreResponseDTO.class));
        });
        return storeResponseDTOList;
    }

    public Page<Store> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), 1000, pageable.getSort());
        return storeRepository.findAll(pageable);
    }

    public Store create(StoreRequestDTO storeRequestDTO, Allowed allowed) throws BusinessException {
        Store store = storeRepository.findByName(storeRequestDTO.getName()).orElse(null);

        if (Objects.isNull(store)) {
            store = modelMapper.map(storeRequestDTO, Store.class);
            store.setStatus(statusService.findById(storeRequestDTO.getStatusId()));
            store = storeRepository.save(store);
        } else {
            throw new BusinessException(RECORD_EXIST_CODE, RECORD_EXIST + "name");
        }
        return store;
    }

    public Store update(StoreRequestDTO storeRequestDTO, int id, Allowed allowed) throws BusinessException, UnauthorizedException {
        User userLogged = userService.findByNick(tokenService.getUserNick());
        Store store = findById(id);

        if (Allowed.STORE.equals(allowed) && !userLogged.getStore().getId().equals(store.getId())) {
            throw new BusinessException(INSUFFICIENT_PRIVILEGES_CODE, INSUFFICIENT_PRIVILEGES);
        }
        store.setStatus(statusService.findById(storeRequestDTO.getStatusId()));
        modelMapper.map(storeRequestDTO, store);
        store = storeRepository.save(store);
        return store;
    }

    public boolean delete(Integer id) throws BusinessException, UnauthorizedException {
        User userLogged = userService.findByNick(tokenService.getUserNick());
        Store store = findById(id);
        if (userLogged.getRole().getId() == 1) {
            try {
                storeRepository.delete(store);
                return true;
            } catch (Exception e) {
                throw new BusinessException(RECORD_CANNOT_BE_DELETED_CODE, RECORD_CANNOT_BE_DELETED);
            }
        } else {
            throw new BusinessException(INSUFFICIENT_PRIVILEGES_CODE, INSUFFICIENT_PRIVILEGES);
        }
    }
}
