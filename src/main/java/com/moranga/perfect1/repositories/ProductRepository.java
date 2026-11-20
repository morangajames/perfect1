package com.moranga.perfect1.repositories;

import com.moranga.perfect1.models.Product;
import com.moranga.perfect1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySeller(User seller);
}