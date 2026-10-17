package com.shopsphere.dao;

import com.shopsphere.model.Review;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Access Object interface for Review entities.
 */
public interface ReviewDAO {
    List<Review> findByProductId(int productId);
    Review findByUserAndProduct(int userId, int productId);
    int insert(Review review);
    BigDecimal calculateAverageRating(int productId);
}
