package com.shopsphere.dao;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of CartDAO using PreparedStatements and try-with-resources.
 */
public class CartDAOImpl implements CartDAO {

    @Override
    public List<CartItem> findByUserId(int userId) {
        String sql = "SELECT c.id, c.user_id, c.product_id, c.quantity, c.created_at, " +
                     "p.category_id, cat.name AS category_name, p.name AS product_name, p.brand, p.description, " +
                     "p.price, p.discount_percentage, p.stock, p.image_url, p.rating, p.status " +
                     "FROM cart c " +
                     "JOIN products p ON c.product_id = p.id " +
                     "JOIN categories cat ON p.category_id = cat.id " +
                     "WHERE c.user_id = ? ORDER BY c.id DESC";
        List<CartItem> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
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
            throw new ShopSphereException("Error finding cart items for user: " + userId, e);
        }
        return list;
    }

    @Override
    public CartItem findByUserAndProduct(int userId, int productId) {
        String sql = "SELECT id, user_id, product_id, quantity, created_at FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CartItem(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding cart item for user: " + userId + ", product: " + productId, e);
        }
        return null;
    }

    @Override
    public boolean addItem(int userId, int productId, int quantity) {
        String sql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setInt(4, quantity);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error adding item to cart", e);
        }
    }

    @Override
    public boolean updateQuantity(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            return removeItem(userId, productId);
        }
        String sql = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating cart quantity", e);
        }
    }

    @Override
    public boolean removeItem(int userId, int productId) {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error removing item from cart", e);
        }
    }

    @Override
    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error clearing cart for user: " + userId, e);
        }
    }

    @Override
    public boolean clearCart(Connection conn, int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error clearing cart with connection for user: " + userId, e);
        }
    }

    @Override
    public int getCartCount(int userId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error getting cart count for user: " + userId, e);
        }
        return 0;
    }
}
