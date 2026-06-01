package com.bookshop.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookshop.domain.BillingAddress;
import com.bookshop.domain.CartItem;
import com.bookshop.domain.Order;
import com.bookshop.domain.Payment;
import com.bookshop.domain.ShippingAddress;
import com.bookshop.domain.ShoppingCart;
import com.bookshop.domain.User;
import com.bookshop.domain.UserPayment;
import com.bookshop.domain.UserShipping;
import com.bookshop.service.BillingAddressService;
import com.bookshop.service.CartItemService;
import com.bookshop.service.OrderService;
import com.bookshop.service.PaymentService;
import com.bookshop.service.ShippingAddressService;
import com.bookshop.service.ShoppingCartService;
import com.bookshop.service.UserPaymentService;
import com.bookshop.service.UserService;
import com.bookshop.service.UserShippingService;
import com.bookshop.utility.MailConstructor;
import com.bookshop.utility.USConstants;

@Controller
public class CheckoutController {

    // ❌ REMOVE shared mutable fields (not thread-safe)
    // private ShippingAddress shippingAddress = new ShippingAddress();
    // private BillingAddress billingAddress = new BillingAddress();
    // private Payment payment = new Payment();

    @Autowired private JavaMailSender mailSender;
    @Autowired private MailConstructor mailConstructor;
    @Autowired private UserService userService;
    @Autowired private CartItemService cartItemService;
    @Autowired private ShoppingCartService shoppingCartService;
    @Autowired private ShippingAddressService shippingAddressService;
    @Autowired private BillingAddressService billingAddressService;
    @Autowired private PaymentService paymentService;
    @Autowired private UserShippingService userShippingService;
    @Autowired private UserPaymentService userPaymentService;
    @Autowired private OrderService orderService;

    // =========================
    // CHECKOUT PAGE
    // =========================
    @RequestMapping("/checkout")
    public String checkout(
            @RequestParam("id") Long cartId,
            @RequestParam(value = "missingRequiredField", required = false, defaultValue = "false") boolean missingRequiredField,
            Model model,
            Principal principal) {

        if (principal == null) return "redirect:/login";

        User user = userService.findByUsername(principal.getName());

        if (!cartId.equals(user.getShoppingCart().getId())) {
            return "badRequestPage";
        }

        List<CartItem> cartItemList =
                cartItemService.findByShoppingCart(user.getShoppingCart());

        if (cartItemList.isEmpty()) {
            model.addAttribute("emptyCart", true);
            return "forward:/shoppingCart/cart";   // ✅ fixed typo
        }

        for (CartItem cartItem : cartItemList) {
            if (cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
                model.addAttribute("notEnoughStock", true);
                return "forward:/shoppingCart/cart";
            }
        }

        List<UserShipping> userShippingList = user.getUserShippingList();
        List<UserPayment> userPaymentList = user.getUserPaymentList();

        model.addAttribute("userShippingList", userShippingList);
        model.addAttribute("userPaymentList", userPaymentList);
        model.addAttribute("emptyPaymentList", userPaymentList.isEmpty());
        model.addAttribute("emptyShippingList", userShippingList.isEmpty());

        // ✅ create fresh objects per request
        ShippingAddress shippingAddress = new ShippingAddress();
        BillingAddress billingAddress = new BillingAddress();
        Payment payment = new Payment();

        for (UserShipping us : userShippingList) {
            if (us.isUserShippingDefault()) {
                shippingAddressService.setByUserShipping(us, shippingAddress);
            }
        }

        for (UserPayment up : userPaymentList) {
            if (up.isDefaultPayment()) {
                paymentService.setByUserPayment(up, payment);
                billingAddressService.setByUserBilling(up.getUserBilling(), billingAddress);
            }
        }

        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("billingAddress", billingAddress);
        model.addAttribute("payment", payment);
        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("shoppingCart", user.getShoppingCart());

        List<String> stateList = USConstants.listOfUSStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);

        model.addAttribute("classActiveShipping", true);
        model.addAttribute("missingRequiredField", missingRequiredField);

        return "checkout";
    }

    // =========================
    // CHECKOUT POST
    // =========================
    @PostMapping("/checkout")
    public String checkoutPost(
            @ModelAttribute ShippingAddress shippingAddress,
            @ModelAttribute BillingAddress billingAddress,
            @ModelAttribute Payment payment,
            @RequestParam("billingSameAsShipping") String billingSameAsShipping,
            @RequestParam("shippingMethod") String shippingMethod,
            Principal principal,
            Model model) {

        if (principal == null) return "redirect:/login";

        User user = userService.findByUsername(principal.getName());
        ShoppingCart shoppingCart = user.getShoppingCart();

        List<CartItem> cartItemList =
                cartItemService.findByShoppingCart(shoppingCart);

        model.addAttribute("cartItemList", cartItemList);

        // ✅ copy shipping → billing
        if ("true".equals(billingSameAsShipping)) {
            billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
            billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
            billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
            billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
            billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
            billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
            billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());
        }

        // ✅ required field validation
        if (isEmpty(shippingAddress.getShippingAddressStreet1())
                || isEmpty(shippingAddress.getShippingAddressCity())
                || isEmpty(shippingAddress.getShippingAddressState())
                || isEmpty(shippingAddress.getShippingAddressName())
                || isEmpty(shippingAddress.getShippingAddressZipcode())
                || isEmpty(payment.getCardNumber())
                || payment.getCvc() == 0
                || isEmpty(billingAddress.getBillingAddressStreet1())
                || isEmpty(billingAddress.getBillingAddressCity())
                || isEmpty(billingAddress.getBillingAddressState())
                || isEmpty(billingAddress.getBillingAddressName())
                || isEmpty(billingAddress.getBillingAddressZipcode())) {

            return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";
        }

        Order order = orderService.createOrder(
                shoppingCart,
                shippingAddress,
                billingAddress,
                payment,
                shippingMethod,
                user);

        mailSender.send(
                mailConstructor.constructOrderConfirmationEmail(
                        user, order, Locale.ENGLISH));

        shoppingCartService.clearShoppingCart(shoppingCart);

        LocalDate estimatedDelivery =
                shippingMethod.equals("groundShipping")
                        ? LocalDate.now().plusDays(5)
                        : LocalDate.now().plusDays(3);

        model.addAttribute("estimatedDeliveryDate", estimatedDelivery);

        return "orderSubmittedPage";
    }

    // =========================
    // SET SHIPPING
    // =========================
    @RequestMapping("/setShippingAddress")
    public String setShippingAddress(
            @RequestParam Long userShippingId,
            Principal principal,
            Model model) {

        if (principal == null) return "redirect:/login";

        User user = userService.findByUsername(principal.getName());
        UserShipping us = userShippingService.findById(userShippingId);

        if (!us.getUser().getId().equals(user.getId())) {
            return "badRequestPage";
        }

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddressService.setByUserShipping(us, shippingAddress);

        return "redirect:/checkout?id=" + user.getShoppingCart().getId();
    }

    // =========================
    // SET PAYMENT
    // =========================
    @RequestMapping("/setPaymentMethod")
    public String setPaymentMethod(
            @RequestParam Long userPaymentId,
            Principal principal) {

        if (principal == null) return "redirect:/login";

        User user = userService.findByUsername(principal.getName());
        UserPayment up = userPaymentService.findById(userPaymentId);

        if (!up.getUser().getId().equals(user.getId())) {
            return "badRequestPage";
        }

        return "redirect:/checkout?id=" + user.getShoppingCart().getId();
    }

    // =========================
    // HELPER
    // =========================
    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
