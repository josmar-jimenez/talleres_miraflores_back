package com.inventory_system.backend.service;

import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.model.Status;
import com.inventory_system.backend.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.inventory_system.backend.util.InventorySystemConstant.RECORD_NOT_FOUND;
import static com.inventory_system.backend.util.InventorySystemConstant.RECORD_NOT_FOUND_CODE;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    public List<Status> getStatus() {
        return statusRepository.findAll();
    }

    public Status findById(int id) throws BusinessException {
        return statusRepository.findById(id).orElseThrow(() ->
                new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND));

    }
}
