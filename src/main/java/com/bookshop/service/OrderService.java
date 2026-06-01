package com.bookshop.service;

import com.bookshop.domain.BillingAddress;
import com.bookshop.domain.Order;
import com.bookshop.domain.Payment;
import com.bookshop.domain.ShippingAddress;
import com.bookshop.domain.ShoppingCart;
import com.bookshop.domain.User;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart,
			ShippingAddress shippingAddress,
			BillingAddress billingAddress,
			Payment payment,
			String shippingMethod,
			User user);
	
	Order findOne(Long id);

}
