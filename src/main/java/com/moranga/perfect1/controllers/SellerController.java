package com.moranga.perfect1.controllers;

import com.moranga.perfect1.models.Product;
import com.moranga.perfect1.models.User;
import com.moranga.perfect1.repositories.ProductRepository;
import com.moranga.perfect1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Show seller dashboard (list of their products)
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User seller = userRepository.findByEmail(authentication.getName()).orElseThrow();
        List<Product> myProducts = productRepository.findBySeller(seller);
        model.addAttribute("products", myProducts);
        return "seller_dashboard";
    }

    // 2. Show "Add Product" form
    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add_product";
    }

    // 3. Handle form submission
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageName") String imageName,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        User seller = userRepository.findByEmail(authentication.getName()).orElseThrow();
        product.setSeller(seller);

        // Add image name (assumes image already exists in /static/images)
        if (imageName != null && !imageName.isBlank()) {
            product.getImageNames().add(imageName);
        }

        productRepository.save(product);
        redirectAttributes.addFlashAttribute("success", "Product added successfully!");
        return "redirect:/seller/dashboard";
    }

    // 4. Delete a product (optional but useful)
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Authentication authentication) {
        Product product = productRepository.findById(id).orElseThrow();
        User seller = userRepository.findByEmail(authentication.getName()).orElseThrow();

        // Safety: only allow deleting own products
        if (product.getSeller() != null && product.getSeller().getId().equals(seller.getId())) {
            productRepository.delete(product);
        }
        return "redirect:/seller/dashboard";
    }
}