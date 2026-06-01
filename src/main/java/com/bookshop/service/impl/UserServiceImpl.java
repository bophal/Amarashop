package com.bookshop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookshop.domain.ShoppingCart;
import com.bookshop.domain.User;
import com.bookshop.domain.UserBilling;
import com.bookshop.domain.UserPayment;
import com.bookshop.domain.UserShipping;
import com.bookshop.domain.security.PasswordResetToken;
import com.bookshop.domain.security.UserRole;
import com.bookshop.repository.PasswordResetTokenRepository;
import com.bookshop.repository.RoleRepository;
import com.bookshop.repository.UserPaymentRepository;
import com.bookshop.repository.UserRepository;
import com.bookshop.repository.UserShippingRepository;
import com.bookshop.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG =
            LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Autowired
    private UserShippingRepository userShippingRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public void createPasswordResetTokenForUser(
            final User user, final String token) {

        PasswordResetToken myToken =
                new PasswordResetToken(token, user);

        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user) {

        // Create shopping cart if null
        if (user.getShoppingCart() == null) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            user.setShoppingCart(shoppingCart);
        }

        return userRepository.save(user);
    }

    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {

        User localUser =
                userRepository.findByUsername(user.getUsername());

        if (localUser != null) {

            LOG.info("User {} already exists.",
                    user.getUsername());

            // Fix old users without cart
            if (localUser.getShoppingCart() == null) {

                ShoppingCart shoppingCart =
                        new ShoppingCart();

                shoppingCart.setUser(localUser);
                localUser.setShoppingCart(shoppingCart);

                localUser = userRepository.save(localUser);
            }

        } else {

            // Save roles
            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);

            // Create shopping cart
            ShoppingCart shoppingCart =
                    new ShoppingCart();

            shoppingCart.setUser(user);
            user.setShoppingCart(shoppingCart);

            // Initialize lists
            user.setUserShippingList(
                    new ArrayList<UserShipping>());

            user.setUserPaymentList(
                    new ArrayList<UserPayment>());

            localUser = userRepository.save(user);
        }

        return localUser;
    }

    @Override
    public User save(User user) {

        // Ensure cart exists
        if (user.getShoppingCart() == null) {

            ShoppingCart shoppingCart =
                    new ShoppingCart();

            shoppingCart.setUser(user);
            user.setShoppingCart(shoppingCart);
        }

        return userRepository.save(user);
    }

    @Override
    public void updateUserBilling(
            UserBilling userBilling,
            UserPayment userPayment,
            User user) {

        userPayment.setUser(user);
        userPayment.setUserBilling(userBilling);
        userPayment.setDefaultPayment(true);

        userBilling.setUserPayment(userPayment);

        user.getUserPaymentList().add(userPayment);

        save(user);
    }

    @Override
    public void updateUserShipping(
            UserShipping userShipping,
            User user) {

        userShipping.setUser(user);
        userShipping.setUserShippingDefault(true);

        user.getUserShippingList()
                .add(userShipping);

        save(user);
    }

    @Override
    public void setUserDefaultPayment(
            Long userPaymentId,
            User user) {

        List<UserPayment> userPaymentList =
                (List<UserPayment>)
                        userPaymentRepository.findAll();

        for (UserPayment userPayment :
                userPaymentList) {

            userPayment.setDefaultPayment(
                    userPayment.getId()
                            .equals(userPaymentId));

            userPaymentRepository.save(userPayment);
        }
    }

    @Override
    public void setUserDefaultShipping(
            Long userShippingId,
            User user) {

        List<UserShipping> userShippingList =
                (List<UserShipping>)
                        userShippingRepository.findAll();

        for (UserShipping userShipping :
                userShippingList) {

            userShipping.setUserShippingDefault(
                    userShipping.getId()
                            .equals(userShippingId));

            userShippingRepository.save(userShipping);
        }
    }

    @Override
    public User findOne(Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

	@Override
	public void registerNewUser(String username, String email) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}