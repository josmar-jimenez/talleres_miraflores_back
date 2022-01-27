package com.inventory_system.backend.service;

import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.RoleAction;
import com.inventory_system.backend.model.User;
import com.inventory_system.backend.repository.RoleActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.inventory_system.backend.util.InventorySystemConstant.INSUFFICIENT_PRIVILEGES;
import static com.inventory_system.backend.util.InventorySystemConstant.INSUFFICIENT_PRIVILEGES_CODE;

@Service
public class RoleOperativeActionService {

    @Autowired
    private RoleActionRepository roleActionRepository;
    @Autowired
    TokenService tokenService;
    @Autowired
    UserService userService;

    public List<RoleAction> findByRoleId(int id) {
        return roleActionRepository.findByRoleId(id);
    }

    public Allowed checkRoleOperativeAndAction(int operative, int action) throws UnauthorizedException, BusinessException {

        User user = userService.findByNick(tokenService.getUserNick());
        return findByRoleId(user.getRole().getId()).stream()
                .filter(roleAction ->
                        roleAction.getOperative().getId() == operative && roleAction.getAction().getId() == action)
                .findFirst().orElseThrow(() -> new BusinessException(INSUFFICIENT_PRIVILEGES_CODE, INSUFFICIENT_PRIVILEGES))
                .getAllowed();
    }
}
