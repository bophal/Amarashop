package com.bookshop.service;

import com.bookshop.domain.Payment;
import com.bookshop.domain.UserPayment;

public interface PaymentService {

	Payment setByUserPayment(UserPayment userPayment, Payment payment);

}
