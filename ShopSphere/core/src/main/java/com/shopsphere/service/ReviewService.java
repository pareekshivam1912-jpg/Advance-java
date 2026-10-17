package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Review;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service managing customer reviews and product rating aggregations.
 */
public class ReviewService {

    private final ReviewDAO reviewDAO;
    private final ProductDAO productDAO;

    public ReviewService(ReviewDAO reviewDAO, ProductDAO productDAO) {
        this.reviewDAO = reviewDAO;
        this.productDAO = productDAO;
    }

    public List<Review> getProductReviews(int productId) {
        return reviewDAO.findByProductId(productId);
    }

    public Review getUserReview(int userId, int productId) {
        return reviewDAO.findByUserAndProduct(userId, productId);
    }

    public Review addReview(int userId, int productId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new ShopSphereException("Rating must be between 1 and 5 stars.");
        }
        if (reviewDAO.findByUserAndProduct(userId, productId) != null) {
            throw new ShopSphereException("You have already reviewed this product.");
        }

        Review r = new Review(userId, productId, rating, comment != null ? comment.trim() : "");
        int id = reviewDAO.insert(r);
        r.setId(id);

        // Recompute average rating and update product
        BigDecimal newAvg = reviewDAO.calculateAverageRating(productId);
        productDAO.updateRating(productId, newAvg);

        return r;
    }
}
