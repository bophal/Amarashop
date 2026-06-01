package com.bookshop.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bookshop.domain.User;
import com.bookshop.domain.UserBilling;
import com.bookshop.domain.UserPayment;
import com.bookshop.domain.UserShipping;
import com.bookshop.domain.security.PasswordResetToken;
import com.bookshop.domain.security.UserRole;

public interface UserService {
PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final User user, final String token);
	
	//User findByUsername(String username);
	
	User findByEmail (String email);
	
	Optional<User> findById(Long id);
	
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	//User save(User user);
	//User createUser(User user);
	
	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
	
	void updateUserShipping(UserShipping userShipping, User user);
	
	void setUserDefaultPayment(Long userPaymentId, User user);
	
	void setUserDefaultShipping(Long userShippingId, User user);

	User findOne(Long id);

	void registerNewUser(String username, String email);

	User createUser(User user);        // ← UserServiceImpl must have this
    User save(User user);              // ← UserServiceImpl must have this
    User findByUsername(String username);
    //User findByEmail(String email);
    List<User> findAll();
}
