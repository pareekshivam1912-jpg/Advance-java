package com.shopsphere.dao;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Product;
import com.shopsphere.model.WishlistItem;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of WishlistDAO using PreparedStatements and try-with-resources.
 */
public class WishlistDAOImpl implements WishlistDAO {

    @Override
    public List<WishlistItem> findByUserId(int userId) {
        String sql = "SELECT w.id, w.user_id, w.product_id, w.created_at, " +
                     "p.category_id, cat.name AS category_name, p.name AS product_name, p.brand, p.description, " +
                     "p.price, p.discount_percentage, p.stock, p.image_url, p.rating, p.status " +
                     "FROM wishlist w " +
                     "JOIN products p ON w.product_id = p.id " +
                     "JOIN categories cat ON p.category_id = cat.id " +
                     "WHERE w.user_id = ? ORDER BY w.id DESC";
        List<WishlistItem> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WishlistItem item = new WishlistItem(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("product_id"),
                            rs.getTimestamp("created_at")
                    );
                    Product p = new Product(
                            rs.getInt("product_id"),
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            rs.getString("product_name"),
                            rs.getString("brand"),
                            rs.getString("description"),
                            rs.getBigDecimal("price"),
                            rs.getBigDecimal("discount_percentage"),
                            rs.getInt("stock"),
                            rs.getString("image_url"),
                            rs.getBigDecimal("rating"),
                            rs.getString("status"),
                            null
                    );
                    item.setProduct(p);
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding wishlist items for user: " + userId, e);
        }
        return list;
    }

    @Override
    public boolean addItem(int userId, int productId) {
        String sql = "INSERT IGNORE INTO wishlist (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error adding item to wishlist", e);
        }
    }

    @Override
    public boolean removeItem(int userId, int productId) {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error removing item from wishlist", e);
        }
    }

    @Override
    public boolean isInWishlist(int userId, int productId) {
        String sql = "SELECT id FROM wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error checking wishlist item", e);
        }
    }

    @Override
    public int getWishlistCount(int userId) {
        String sql = "SELECT COUNT(*) FROM wishlist WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error counting wishlist items", e);
        }
        return 0;
    }
}
