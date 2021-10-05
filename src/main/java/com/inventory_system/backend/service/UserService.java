package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.request.user.UserRequestDTO;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.User;
import com.inventory_system.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    TokenService tokenService;
    @Autowired
    private ModelMapper modelMapper;

    public User findByNick(String nick) throws UnauthorizedException {
        return userRepository.findByNick(nick).orElseThrow(UnauthorizedException::new);
    }

    public User findById(Integer id) throws BusinessException {
        return userRepository.findById(id).orElseThrow(()
                -> new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND));
    }

    public Page<User> findAll(Pageable pageable, Allowed allowed) throws BusinessException, UnauthorizedException {
        if(Allowed.ALL.equals(allowed)){
            return userRepository.findAll(pageable);
        } else {
            User userLogged = findByNick(tokenService.getUserNick());
            if(Allowed.STORE.equals(allowed)){
                return userRepository.findByStore(userLogged.getStore(),pageable);
            } else if(Allowed.USER.equals(allowed)){
                return userRepository.findById(userLogged.getId(),pageable);
            }
        }
        return null;
    }

     public User create(UserRequestDTO userRequestDTO, Allowed allowed) throws BusinessException, UnauthorizedException {
        User user = userRepository.findByNick(userRequestDTO.getNick()).orElse(null);

       if( Objects.isNull(user)){
           user = modelMapper.map(userRequestDTO, User.class);
           user.setStatus(statusService.findById(userRequestDTO.getStatus_id()).orElseThrow(()
                   -> new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND)));
           if(Allowed.ALL.equals(allowed)) {
               user.setStore(storeService.findById(userRequestDTO.getStore_id()).orElseThrow(()
                       -> new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND)));
           }else{
               User userLogged = findByNick(tokenService.getUserNick());
               user.setStore(userLogged.getStore());
           }
           setUserRole(user,userRequestDTO.getRole_id());
           user =userRepository.save(user);
        }else{
          throw  new BusinessException(RECORD_EXIST_CODE,RECORD_EXIST+"nick");
       }
        return user;
    }

    public User update(UserRequestDTO userRequestDTO, int id, Allowed allowed) throws BusinessException, UnauthorizedException {
        User userLogged = findByNick(tokenService.getUserNick());
        User user;
        if(Allowed.USER.equals(allowed)){
            user = userLogged;
        } else {
            user = userRepository.findById(id).orElseThrow(()
                    -> new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND));

            if (Allowed.ALL.equals(allowed)) {
                user.setStatus(statusService.findById(userRequestDTO.getStatus_id()).orElseThrow(()
                        -> new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND)));
                user.setStore(storeService.findById(userRequestDTO.getStatus_id()).orElseThrow(()
                        -> new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND)));
                setUserRole(user, userRequestDTO.getRole_id());
            } else {
                if (Allowed.STORE.equals(allowed)) {
                    user.setStatus(statusService.findById(userRequestDTO.getStatus_id()).orElseThrow(()
                            -> new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND)));
                    user.setStore(userLogged.getStore());
                    setUserRole(user, userRequestDTO.getRole_id());
                }
            }
        }
        modelMapper.map(userRequestDTO, user);
        user =userRepository.save(user);
        return user;
    }

    private void setUserRole(User user, int roleId) throws BusinessException {
        if(roleId==1){
            throw new BusinessException(OPERATION_NOT_ALLOWED_CODE,OPERATION_NOT_ALLOWED);
        }
        user.setRole(roleService.findById(roleId).orElseThrow(() -> new BusinessException(RECORD_NOT_FOUND_CODE,RECORD_NOT_FOUND)));
    }
}
