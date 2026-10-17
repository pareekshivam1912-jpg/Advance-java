package com.shopsphere.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 * Domain entity representing a product in the ShopSphere catalog.
 * Implements Serializable for RMI and Socket inventory transmission.
 */
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int categoryId;
    private String categoryName;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private int stock;
    private String imageUrl;
    private BigDecimal rating;
    private String status; // 'ACTIVE' or 'INACTIVE'
    private Timestamp createdAt;

    /**
     * RTU Syllabus Demo: transient field excluded from Java Object Serialization streams.
     * When serialized to product.ser, this value will be null upon deserialization.
     */
    private transient String sessionViewToken;

    public Product() {
        this.price = BigDecimal.ZERO;
        this.discountPercentage = BigDecimal.ZERO;
        this.rating = BigDecimal.ZERO;
        this.status = "ACTIVE";
    }

    public Product(int categoryId, String name, String brand, String description,
                   BigDecimal price, BigDecimal discountPercentage, int stock,
                   String imageUrl, BigDecimal rating, String status) {
        this.categoryId = categoryId;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price != null ? price : BigDecimal.ZERO;
        this.discountPercentage = discountPercentage != null ? discountPercentage : BigDecimal.ZERO;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.rating = rating != null ? rating : BigDecimal.ZERO;
        this.status = status != null ? status : "ACTIVE";
    }

    public Product(int id, int categoryId, String categoryName, String name, String brand,
                   String description, BigDecimal price, BigDecimal discountPercentage,
                   int stock, String imageUrl, BigDecimal rating, String status, Timestamp createdAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price != null ? price : BigDecimal.ZERO;
        this.discountPercentage = discountPercentage != null ? discountPercentage : BigDecimal.ZERO;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.rating = rating != null ? rating : BigDecimal.ZERO;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getSessionViewToken() {
        return sessionViewToken;
    }

    public void setSessionViewToken(String sessionViewToken) {
        this.sessionViewToken = sessionViewToken;
    }

    /**
     * Calculates the discounted selling price: price - (price * discount / 100).
     */
    public BigDecimal getDiscountedPrice() {
        if (price == null || BigDecimal.ZERO.compareTo(price) >= 0) {
            return BigDecimal.ZERO;
        }
        if (discountPercentage == null || BigDecimal.ZERO.compareTo(discountPercentage) >= 0) {
            return price.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal discountFactor = discountPercentage.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal discountVal = price.multiply(discountFactor);
        return price.subtract(discountVal).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isOutOfStock() {
        return stock <= 0;
    }

    public boolean isLowStock(int threshold) {
        return stock > 0 && stock <= threshold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", status='" + status + '\'' +
                '}';
    }
}
