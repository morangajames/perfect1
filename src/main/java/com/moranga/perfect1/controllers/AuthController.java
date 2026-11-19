package com.moranga.perfect1.controllers;

import com.moranga.perfect1.models.User;
import com.moranga.perfect1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Show the registration page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register"; // This looks for register.html
    }

    // Handle the form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/login?success"; // Go to login page if successful
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Stay on page and show error
        }
    }

    // Show the login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // This looks for login.html
    }
}