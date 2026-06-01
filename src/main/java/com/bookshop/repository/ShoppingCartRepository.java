package com.bookshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.bookshop.domain.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long>{

}
