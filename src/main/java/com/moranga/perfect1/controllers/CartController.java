package com.moranga.perfect1.controllers;

import com.moranga.perfect1.models.CartItem;
import com.moranga.perfect1.models.Product;
import com.moranga.perfect1.models.User;
import com.moranga.perfect1.repositories.CartItemRepository;
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
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Add a product to the cart
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        List<CartItem> userCart = cartItemRepository.findByUser(user);
        CartItem existingItem = null;

        for (CartItem item : userCart) {
            if (item.getProduct().getId().equals(productId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(1);
            cartItemRepository.save(newItem);
        }

        // Add success message to be shown on the cart page (or index page)
        redirectAttributes.addFlashAttribute("successMessage", "Item added to cart successfully!");

        return "redirect:/cart";
        // Note: If you want to stay on the home page after adding, change this to "redirect:/"
    }

    // 2. Show the cart page
    @GetMapping
    public String viewCart(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        model.addAttribute("cartItems", cartItems);

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("total", total);

        return "cart";
    }

    // 3. Remove an item from the cart
    @PostMapping("/remove/{cartItemId}")
    public String removeFromCart(@PathVariable Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return "redirect:/cart";
    }

    // 4. Increase Quantity (+)
    @PostMapping("/increase/{cartItemId}")
    public String increaseQuantity(@PathVariable Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow();
        item.setQuantity(item.getQuantity() + 1);
        cartItemRepository.save(item);
        return "redirect:/cart";
    }

    // 5. Decrease Quantity (-)
    @PostMapping("/decrease/{cartItemId}")
    public String decreaseQuantity(@PathVariable Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow();
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            cartItemRepository.save(item);
        } else {
            // If the quantity is 1 and they click minus, remove the item
            cartItemRepository.deleteById(cartItemId);
        }
        return "redirect:/cart";
    }
}