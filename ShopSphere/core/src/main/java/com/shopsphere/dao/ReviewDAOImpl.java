package com.shopsphere.dao;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Review;
import com.shopsphere.util.DBUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of ReviewDAO using PreparedStatements and try-with-resources.
 */
public class ReviewDAOImpl implements ReviewDAO {

    @Override
    public List<Review> findByProductId(int productId) {
        String sql = "SELECT r.id, r.user_id, u.name AS user_name, r.product_id, r.rating, r.comment, r.created_at " +
                     "FROM reviews r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "WHERE r.product_id = ? ORDER BY r.id DESC";
        List<Review> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Review(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("user_name"),
                            rs.getInt("product_id"),
                            rs.getInt("rating"),
                            rs.getString("comment"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding reviews for product: " + productId, e);
        }
        return list;
    }

    @Override
    public Review findByUserAndProduct(int userId, int productId) {
        String sql = "SELECT r.id, r.user_id, u.name AS user_name, r.product_id, r.rating, r.comment, r.created_at " +
                     "FROM reviews r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "WHERE r.user_id = ? AND r.product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Review(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("user_name"),
                            rs.getInt("product_id"),
                            rs.getInt("rating"),
                            rs.getString("comment"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding review for user: " + userId + ", product: " + productId, e);
        }
        return null;
    }

    @Override
    public int insert(Review review) {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        review.setId(generatedId);
                        return generatedId;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error inserting review for product: " + review.getProductId(), e);
        }
        return -1;
    }

    @Override
    public BigDecimal calculateAverageRating(int productId) {
        String sql = "SELECT COALESCE(AVG(rating), 0.0) FROM reviews WHERE product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1).setScale(2, RoundingMode.HALF_UP);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error calculating average rating for product: " + productId, e);
        }
        return BigDecimal.ZERO;
    }
}
