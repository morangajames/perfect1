package com.moranga.perfect1.repositories;

import com.moranga.perfect1.models.CartItem;
import com.moranga.perfect1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // This is the magic method that allows us to find a specific user's cart
    List<CartItem> findByUser(User user);
}
