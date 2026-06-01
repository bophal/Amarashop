package com.bookshop.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bookshop.domain.User;

public interface UserRepository extends CrudRepository<User, Long>{
	User findByUsername(String username);
	
	User findByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
	
	//User findById(Long id);

}
