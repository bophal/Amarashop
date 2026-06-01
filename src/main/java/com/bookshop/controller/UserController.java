package com.bookshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.bookshop.service.UserService;       // correct package for UserService
import org.springframework.ui.Model;           // correct Spring MVC Model
//import com.bookshop.domain.UserService;

//import ch.qos.logback.core.model.Model;

@Controller
public class UserController {
	@Autowired
    private UserService userService;

    // show registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "register";
    }

    // handle form submission
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            Model model) {
        try {
            userService.registerNewUser(username, email);
            model.addAttribute("successMessage",
                "Account created! A temporary password has been sent to your email.");
            return "register";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

}
