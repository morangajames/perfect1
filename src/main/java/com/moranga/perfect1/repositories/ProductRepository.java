package com.moranga.perfect1.repositories;

import com.moranga.perfect1.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}