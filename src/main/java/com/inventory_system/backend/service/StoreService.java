package com.inventory_system.backend.service;

import com.inventory_system.backend.model.Store;
import com.inventory_system.backend.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public Optional<Store> findById(int id){
        return storeRepository.findById(id);
    }
}
