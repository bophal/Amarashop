package com.bookshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.bookshop.domain.UserPayment;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long>{

}
