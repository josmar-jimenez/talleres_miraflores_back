package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.request.user.UserRequestDTO;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.User;
import com.inventory_system.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.inventory_system.backend.util.InventorySystemConstant.*;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private ModelMapper modelMapper;

    public User findByNick(String nick) throws UnauthorizedException {
        return userRepository.findByNick(nick).orElseThrow(() -> new UnauthorizedException());
    }

    public User findById(Integer id) throws BusinessException {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND));
    }

    public User create(UserRequestDTO userRequestDTO) throws BusinessException {
        User user = userRepository.findByNick(userRequestDTO.getNick()).orElse(null);
       if( Objects.isNull(user)){
           user = modelMapper.map(userRequestDTO, User.class);
           user.setRole(roleService.findById(userRequestDTO.getStatus_id()).orElseThrow(() -> new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND)));
           user.setStatus(statusService.findById(userRequestDTO.getStatus_id()).orElseThrow(() -> new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND)));
           user.setStore(storeService.findById(userRequestDTO.getStatus_id()).orElseThrow(() -> new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND)));
           user =userRepository.save(user);
        }else{
          throw  new BusinessException(RECORD_EXIST_CODE,RECORD_EXIST+"nick");
       }
        return user;
    }
}
