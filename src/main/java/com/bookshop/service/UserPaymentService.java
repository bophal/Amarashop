package com.bookshop.service;

import com.bookshop.domain.UserPayment;

public interface UserPaymentService {
UserPayment findById(Long id);
	
	void removeById(Long id);

}
