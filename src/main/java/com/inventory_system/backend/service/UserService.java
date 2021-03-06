package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.request.user.UserRequestDTO;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.Tag;
import com.inventory_system.backend.model.User;
import com.inventory_system.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public User findById(Integer id, Allowed allowed) throws BusinessException, UnauthorizedException {

        User user = userRepository.findById(id).orElseThrow(()
                -> new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND));
        if (Allowed.ALL.equals(allowed)) {
            return user;
        } else {
            User userLogged = findByNick(tokenService.getUserNick());
            if (Allowed.STORE.equals(allowed) && !user.getStore().equals(userLogged.getStore())) {
                throw new BusinessException(INSUFFICIENT_PRIVILEGES_CODE, INSUFFICIENT_PRIVILEGES);
            } else if (Allowed.USER.equals(allowed) && !user.getId().equals(userLogged.getId())) {
                throw new BusinessException(INSUFFICIENT_PRIVILEGES_CODE, INSUFFICIENT_PRIVILEGES);
            }
        }
        return user;
    }

    public Page<User> findAll(Pageable pageable, Allowed allowed) throws UnauthorizedException {
        if(pageable.getSort().isEmpty()) {
            Sort sortDefault = Sort.by("id").descending();
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortDefault);
        }
        pageable = PageRequest.of(pageable.getPageNumber(), 1000, pageable.getSort());

        if (Allowed.ALL.equals(allowed)) {
            return userRepository.findAll(pageable);
        } else {
            User userLogged = findByNick(tokenService.getUserNick());
            if (Allowed.STORE.equals(allowed)) {
                return userRepository.findByStore(userLogged.getStore(), pageable);
            } else if (Allowed.USER.equals(allowed)) {
                return userRepository.findById(userLogged.getId(), pageable);
            }
        }
        return null;
    }

    public User create(UserRequestDTO userRequestDTO, Allowed allowed) throws BusinessException, UnauthorizedException {
        User user = userRepository.findByNick(userRequestDTO.getNick()).orElse(null);

        if (Objects.isNull(user)) {
            user = modelMapper.map(userRequestDTO, User.class);
            user.setStatus(statusService.findById(userRequestDTO.getStatusId()));
            if (Allowed.ALL.equals(allowed)) {
                user.setStore(storeService.findById(userRequestDTO.getStatusId()));
            } else {
                User userLogged = findByNick(tokenService.getUserNick());
                user.setStore(userLogged.getStore());
            }
            setUserRole(user, userRequestDTO.getRoleId());
            user = userRepository.save(user);
        } else {
            throw new BusinessException(RECORD_EXIST_CODE, RECORD_EXIST + "nick");
        }
        return user;
    }

    public User update(UserRequestDTO userRequestDTO, int id, Allowed allowed) throws BusinessException, UnauthorizedException {
        User userLogged = findByNick(tokenService.getUserNick());
        User user;
        if (Allowed.USER.equals(allowed)) {
            user = userLogged;
        } else {
            user = userRepository.findById(id).orElseThrow(()
                    -> new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND));

            if (Allowed.ALL.equals(allowed)) {
                user.setStatus(statusService.findById(userRequestDTO.getStatusId()));
                user.setStore(storeService.findById(userRequestDTO.getStoreId()));
                if (!user.getId().equals(userLogged.getId()))
                    setUserRole(user, userRequestDTO.getRoleId());
            } else {
                if (Allowed.STORE.equals(allowed)) {
                    user.setStatus(statusService.findById(userRequestDTO.getStatusId()));
                    user.setStore(userLogged.getStore());
                    setUserRole(user, userRequestDTO.getRoleId());
                }
            }
        }
        /*Unique field don't map*/
        userRequestDTO.setNick(user.getNick());
        modelMapper.map(userRequestDTO, user);
        user = userRepository.save(user);
        return user;
    }

    public boolean delete(Integer id, Allowed allowed) throws BusinessException, UnauthorizedException {
        User user = findById(id, allowed);
        if (user.getRole().getId() != 1) {
            user.setStatus(statusService.findById(4));
            userRepository.save(user);
        }
        return true;
    }

    private void setUserRole(User user, int roleId) throws BusinessException {
        if (roleId == 1) {
            throw new BusinessException(OPERATION_NOT_ALLOWED_CODE, OPERATION_NOT_ALLOWED);
        }
        user.setRole(roleService.findById(roleId).orElseThrow(() -> new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND)));
    }
}
