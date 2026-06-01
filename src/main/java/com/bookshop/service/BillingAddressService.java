package com.bookshop.service;

import com.bookshop.domain.BillingAddress;
import com.bookshop.domain.UserBilling;

public interface BillingAddressService {
	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);

}
