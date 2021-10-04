package com.inventory_system.backend.service;

import com.inventory_system.backend.model.Status;
import com.inventory_system.backend.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    public List<Status> getStatus(){
        return statusRepository.findAll();
    }

    public Optional<Status> findById(int id){
        return statusRepository.findById(id);
    }
}
