package com.bookshop.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;   // ✅ CORRECT MODEL
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookshop.domain.Book;
import com.bookshop.domain.CartItem;
import com.bookshop.domain.ShoppingCart;
import com.bookshop.domain.User;
import com.bookshop.service.BookService;
import com.bookshop.service.CartItemService;
import com.bookshop.service.ShoppingCartService;
import com.bookshop.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @RequestMapping("/cart")
    public String shoppingCart(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        ShoppingCart shoppingCart = user.getShoppingCart();
        
        // ← ADD THIS NULL CHECK
        if (shoppingCart == null) {
            model.addAttribute("cartItemList", new ArrayList<>());
            model.addAttribute("shoppingCart", new ShoppingCart());
            return "shoppingCart";
        }

        List<CartItem> cartItemList =
                cartItemService.findByShoppingCart(shoppingCart);

        shoppingCartService.updateShoppingCart(shoppingCart);

        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("shoppingCart", shoppingCart);

        return "shoppingCart";
    }

    // ✅ FIXED PARAMS
    @PostMapping("/addItem")
    public String addItem(
            @RequestParam("id") Long bookId,
            @RequestParam("qty") int qty,
            Model model,
            Principal principal) {

        User user = userService.findByUsername(principal.getName());
        Book book = bookService.findById(bookId).orElse(null);
        
     // Add null check for book
        if (book == null) {
            return "forward:/bookshelf";  // or wherever makes sense
        }

        if (qty > book.getInStockNumber()) {
            model.addAttribute("notEnoughStock", true);
            return "forward:/bookDetail?id=" + bookId;
        }

        cartItemService.addBookToCartItem(book, user, qty);
        model.addAttribute("addBookSuccess", true);

        return "forward:/bookDetail?id=" + bookId;
    }

    // ✅ FIXED PARAM ANNOTATIONS
    @RequestMapping("/updateCartItem")
    public String updateShoppingCart(
            @RequestParam("id") Long cartItemId,
            @RequestParam("qty") int qty) {

        CartItem cartItem = cartItemService.findById(cartItemId);

        if (cartItem != null) {
            cartItem.setQty(qty);
            cartItemService.updateCartItem(cartItem);
        }

        return "forward:/shoppingCart/cart";
    }

    @RequestMapping("/removeItem")
    public String removeItem(@RequestParam("id") Long id) {
        CartItem cartItem = cartItemService.findById(id);
        if (cartItem != null) {
            cartItemService.removeCartItem(cartItem);
        }

        return "forward:/shoppingCart/cart";
    }
}
