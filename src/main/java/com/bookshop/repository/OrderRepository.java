package com.bookshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.bookshop.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{

}
