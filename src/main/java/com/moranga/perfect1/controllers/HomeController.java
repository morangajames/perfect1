package com.moranga.perfect1.controllers;

import com.moranga.perfect1.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String home(Model model) {
        // Optional: Filter products that are in stock (if you have a 'stock' field)
        // model.addAttribute("products", productRepository.findByStockGreaterThan(0));

        // Currently returns all products (works fine)
        model.addAttribute("products", productRepository.findAll());

        return "index";
    }
}