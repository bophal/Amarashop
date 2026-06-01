package com.bookshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookshop.domain.UserShipping;
import com.bookshop.repository.UserShippingRepository;
import com.bookshop.service.UserShippingService;

@Service
public class UserShippingServiceImpl implements UserShippingService{
	@Autowired
	private UserShippingRepository userShippingRepository;
	
	
	public UserShipping findById(Long id) {
		return userShippingRepository.findById(id).orElse(null);
	}
	
	public void removeById(Long id) {
		userShippingRepository.deleteById(id);
	}

}
