package com.shopsphere.dao;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.OrderItem;
import com.shopsphere.model.Product;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of OrderItemDAO using PreparedStatements and try-with-resources.
 */
public class OrderItemDAOImpl implements OrderItemDAO {

    @Override
    public List<OrderItem> findByOrderId(int orderId) {
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.price_per_unit, oi.quantity, oi.subtotal, " +
                     "p.category_id, cat.name AS category_name, p.name AS product_name, p.brand, p.description, " +
                     "p.image_url, p.rating " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN categories cat ON p.category_id = cat.id " +
                     "WHERE oi.order_id = ? ORDER BY oi.id ASC";
        List<OrderItem> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                            rs.getInt("id"),
                            rs.getInt("order_id"),
                            rs.getInt("product_id"),
                            rs.getBigDecimal("price_per_unit"),
                            rs.getInt("quantity"),
                            rs.getBigDecimal("subtotal")
                    );
                    Product p = new Product();
                    p.setId(rs.getInt("product_id"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setCategoryName(rs.getString("category_name"));
                    p.setName(rs.getString("product_name"));
                    p.setBrand(rs.getString("brand"));
                    p.setDescription(rs.getString("description"));
                    p.setImageUrl(rs.getString("image_url"));
                    p.setRating(rs.getBigDecimal("rating"));
                    item.setProduct(p);
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding items for order: " + orderId, e);
        }
        return list;
    }

    @Override
    public void insertBatch(Connection conn, List<OrderItem> items) {
        String sql = "INSERT INTO order_items (order_id, product_id, price_per_unit, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (OrderItem item : items) {
                ps.setInt(1, item.getOrderId());
                ps.setInt(2, item.getProductId());
                ps.setBigDecimal(3, item.getPricePerUnit());
                ps.setInt(4, item.getQuantity());
                ps.setBigDecimal(5, item.getSubtotal());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new ShopSphereException("Error batch inserting order items", e);
        }
    }
}
