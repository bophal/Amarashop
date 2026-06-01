package com.bookshop.utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bookshop.domain.security.Role;
import com.bookshop.repository.RoleRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
	@Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
    	if (!roleRepository.existsByName("ROLE_USER")) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
    }

}
