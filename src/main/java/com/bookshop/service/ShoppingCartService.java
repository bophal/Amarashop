package com.bookshop.service;

import com.bookshop.domain.ShoppingCart;

public interface ShoppingCartService {
ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);
	
	void clearShoppingCart(ShoppingCart shoppingCart);

}
