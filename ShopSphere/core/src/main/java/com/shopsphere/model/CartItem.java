package com.shopsphere.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Domain entity representing an item in the customer shopping cart.
 */
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private Timestamp createdAt;
    private Product product; // Attached Product details

    public CartItem() {
    }

    public CartItem(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartItem(int id, int userId, int productId, int quantity, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Line subtotal based on product's discounted selling price.
     */
    public BigDecimal getLineTotal() {
        if (product == null) {
            return BigDecimal.ZERO;
        }
        return product.getDiscountedPrice().multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
