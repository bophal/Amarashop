package com.bookshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.bookshop.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role,Integer >{
	Role findByName(String name);

}
