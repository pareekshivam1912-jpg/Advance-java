package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Domain entity representing a customer review and rating for a product.
 */
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private String userName; // Attached for display
    private int productId;
    private int rating; // 1 to 5
    private String comment;
    private Timestamp createdAt;

    public Review() {
    }

    public Review(int userId, int productId, int rating, String comment) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }

    public Review(int id, int userId, String userName, int productId, int rating, String comment, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", rating=" + rating +
                '}';
    }
}
