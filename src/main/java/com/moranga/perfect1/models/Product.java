package com.moranga.perfect1.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    private double price;

    private int stock; // NEW: track inventory

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_name")
    private List<String> imageNames = new ArrayList<>(); // matches your existing field

    // NEW: Link product to the seller who created it
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public List<String> getImageNames() { return imageNames; }
    public void setImageNames(List<String> imageNames) { this.imageNames = imageNames; }

    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }
}