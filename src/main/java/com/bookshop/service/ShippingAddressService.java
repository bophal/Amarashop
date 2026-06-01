package com.bookshop.service;

import com.bookshop.domain.ShippingAddress;
import com.bookshop.domain.UserShipping;

public interface ShippingAddressService {
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);

}
