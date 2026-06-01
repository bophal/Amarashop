package com.bookshop.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookshop.domain.Book;
import com.bookshop.domain.BookToCartItem;
import com.bookshop.domain.CartItem;
import com.bookshop.domain.Order;
import com.bookshop.domain.ShoppingCart;
import com.bookshop.domain.User;
import com.bookshop.repository.BookToCartItemRepository;
import com.bookshop.repository.CartItemRepository;
import com.bookshop.repository.UserRepository;
import com.bookshop.service.CartItemService;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final BookToCartItemRepository bookToCartItemRepository;
    private final UserRepository userRepository;

    public CartItemServiceImpl(
            CartItemRepository cartItemRepository,
            BookToCartItemRepository bookToCartItemRepository,
            UserRepository userRepository) {

        this.cartItemRepository = cartItemRepository;
        this.bookToCartItemRepository = bookToCartItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CartItem> findByShoppingCart(
            ShoppingCart shoppingCart) {

        if (shoppingCart == null) {
            throw new IllegalArgumentException(
                    "ShoppingCart cannot be null");
        }

        return cartItemRepository
                .findByShoppingCart(shoppingCart);
    }

    @Override
    public CartItem updateCartItem(CartItem cartItem) {

        validateCartItem(cartItem);

        cartItem.setSubtotal(
                calcSubtotal(
                        cartItem.getBook().getOurPrice(),
                        cartItem.getQty()
                )
        );

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem addBookToCartItem(
            Book book,
            User user,
            int qty) {

        if (book == null || user == null) {
            throw new IllegalArgumentException(
                    "Book/User cannot be null");
        }

        if (qty <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be > 0");
        }

        // Get shopping cart
        ShoppingCart shoppingCart =
                user.getShoppingCart();

        // Create cart if missing
        if (shoppingCart == null) {

            shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);

            user.setShoppingCart(shoppingCart);

            userRepository.save(user);
        }

        // Find existing cart items
        List<CartItem> cartItemList =
                findByShoppingCart(shoppingCart);

        // Update quantity if book already exists
        for (CartItem cartItem : cartItemList) {

            if (book.getId()
                    .equals(cartItem.getBook().getId())) {

                int newQty =
                        cartItem.getQty() + qty;

                cartItem.setQty(newQty);

                cartItem.setSubtotal(
                        calcSubtotal(
                                book.getOurPrice(),
                                newQty
                        )
                );

                return cartItemRepository
                        .save(cartItem);
            }
        }

        // Create new cart item
        CartItem cartItem = new CartItem();

        cartItem.setShoppingCart(
                shoppingCart);

        cartItem.setBook(book);
        cartItem.setQty(qty);

        cartItem.setSubtotal(
                calcSubtotal(
                        book.getOurPrice(),
                        qty
                )
        );

        cartItem =
                cartItemRepository.save(cartItem);

        // Save relation
        BookToCartItem link =
                new BookToCartItem();

        link.setBook(book);
        link.setCartItem(cartItem);

        bookToCartItemRepository.save(link);

        return cartItem;
    }

    @Override
    public CartItem findById(Long id) {

        if (id == null) {
            return null;
        }

        return cartItemRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public void removeCartItem(
            CartItem cartItem) {

        if (cartItem == null) {
            return;
        }

        bookToCartItemRepository
                .deleteByCartItem(cartItem);

        cartItemRepository
                .delete(cartItem);
    }

    @Override
    public CartItem save(
            CartItem cartItem) {

        validateCartItem(cartItem);

        return cartItemRepository
                .save(cartItem);
    }

    @Override
    public List<CartItem> findByOrder(
            Order order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null");
        }

        return cartItemRepository
                .findByOrder(order);
    }

    // ---------- helpers ----------

    private void validateCartItem(
            CartItem cartItem) {

        if (cartItem == null ||
                cartItem.getBook() == null) {

            throw new IllegalStateException(
                    "CartItem/Book missing");
        }

        if (cartItem.getBook().getOurPrice() <= 0) {

            throw new IllegalStateException(
                    "Book price missing");
        }
    }

    private BigDecimal calcSubtotal(
            Number price,
            int qty) {

        return new BigDecimal(
                price.toString())
                .multiply(
                        BigDecimal.valueOf(qty))
                .setScale(
                        2,
                        RoundingMode.HALF_UP);
    }
}