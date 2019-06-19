package com.kakao.housingfinance.service;

import com.kakao.housingfinance.model.Role;
import com.kakao.housingfinance.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Role 정보를 DB로부터 조회함
     */
    public Collection<Role> findAll() {
        return roleRepository.findAll();
    }

}
