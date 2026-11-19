package com.moranga.perfect1.controllers;

import com.moranga.perfect1.models.Product;
import com.moranga.perfect1.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private ProductRepository productRepository;

    // 1. Show the Add Product Form
    @GetMapping("/dashboard")
    public String sellerDashboard(Model model) {
        model.addAttribute("product", new Product());
        return "seller_dashboard";
    }

    // 2. Handle the form submission (Save product + multiple images)
    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFiles") MultipartFile[] imageFiles) throws IOException {

        String uploadDir = "src/main/resources/static/images/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        List<String> savedImageNames = new ArrayList<>();

        // Loop through all uploaded images
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                savedImageNames.add(fileName);
            }
        }

        product.setImageNames(savedImageNames);
        productRepository.save(product); // Fixed: properly saved to the database

        return "redirect:/seller/dashboard?success";
    }
}