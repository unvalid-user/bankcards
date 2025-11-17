package com.example.bankcards.service;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.RoleName;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.bankcards.util.AppConst.ROLE;
import static com.example.bankcards.util.AppConst.ROLE_NAME;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;


    public Role findRoleByName(RoleName roleName) {
        return roleRepository.findByName(roleName).orElseThrow(() ->
                new ResourceNotFoundException(ROLE, ROLE_NAME, roleName));
    }
}
