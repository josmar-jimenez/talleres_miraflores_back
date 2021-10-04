package com.inventory_system.backend.service;

import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.User;
import com.inventory_system.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.inventory_system.backend.util.InventorySystemConstant.RECORD_NOT_FOUND;
import static com.inventory_system.backend.util.InventorySystemConstant.RECORD_NOT_FOUND_CODE;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByNick(String nick) throws UnauthorizedException {
        return userRepository.findByNick(nick).orElseThrow(() -> new UnauthorizedException());
    }

    public User findById(Integer id) throws BusinessException {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND));
    }
}
