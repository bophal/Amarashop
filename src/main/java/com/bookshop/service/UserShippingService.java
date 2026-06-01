package com.bookshop.service;

import com.bookshop.domain.UserShipping;

public interface UserShippingService {
UserShipping findById(Long id);
	
	void removeById(Long id);

}
