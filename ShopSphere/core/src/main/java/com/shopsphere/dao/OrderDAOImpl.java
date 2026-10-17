package com.shopsphere.dao;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Order;
import com.shopsphere.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of OrderDAO using PreparedStatements and try-with-resources.
 */
public class OrderDAOImpl implements OrderDAO {

    private static final String BASE_SELECT =
            "SELECT id, user_id, address_id, coupon_id, subtotal, discount_amount, shipping_fee, " +
            "total_amount, order_status, payment_method, payment_status, created_at, updated_at FROM orders ";

    @Override
    public Order findById(int id) {
        String sql = BASE_SELECT + "WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding order by id: " + id, e);
        }
        return null;
    }

    @Override
    public List<Order> findByUserId(int userId) {
        String sql = BASE_SELECT + "WHERE user_id = ? ORDER BY id DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding orders for user: " + userId, e);
        }
        return list;
    }

    @Override
    public List<Order> findAll() {
        String sql = BASE_SELECT + "ORDER BY id DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding all orders", e);
        }
        return list;
    }

    @Override
    public List<Order> findByStatus(String status) {
        String sql = BASE_SELECT + "WHERE order_status = ? ORDER BY id DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding orders by status: " + status, e);
        }
        return list;
    }

    @Override
    public int insert(Connection conn, Order order) {
        String sql = "INSERT INTO orders (user_id, address_id, coupon_id, subtotal, discount_amount, " +
                     "shipping_fee, total_amount, order_status, payment_method, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getAddressId());
            if (order.getCouponId() != null && order.getCouponId() > 0) {
                ps.setInt(3, order.getCouponId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setBigDecimal(4, order.getSubtotal());
            ps.setBigDecimal(5, order.getDiscountAmount());
            ps.setBigDecimal(6, order.getShippingFee());
            ps.setBigDecimal(7, order.getTotalAmount());
            ps.setString(8, order.getOrderStatus());
            ps.setString(9, order.getPaymentMethod());
            ps.setString(10, order.getPaymentStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        order.setId(generatedId);
                        return generatedId;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error inserting order with transaction connection", e);
        }
        return -1;
    }

    @Override
    public boolean updateStatus(int id, String orderStatus) {
        String sql = "UPDATE orders SET order_status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderStatus);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating order status for id: " + id, e);
        }
    }

    @Override
    public boolean updatePaymentStatus(int id, String paymentStatus) {
        String sql = "UPDATE orders SET payment_status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paymentStatus);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating order payment status for id: " + id, e);
        }
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error counting orders", e);
        }
        return 0;
    }

    @Override
    public BigDecimal getTotalSales() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0.00) FROM orders WHERE payment_status = 'PAID'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error calculating total sales", e);
        }
        return BigDecimal.ZERO;
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order o = new Order(
                rs.getInt("user_id"),
                rs.getInt("address_id"),
                (Integer) rs.getObject("coupon_id"),
                rs.getBigDecimal("subtotal"),
                rs.getBigDecimal("discount_amount"),
                rs.getBigDecimal("shipping_fee"),
                rs.getBigDecimal("total_amount"),
                rs.getString("order_status"),
                rs.getString("payment_method"),
                rs.getString("payment_status")
        );
        o.setId(rs.getInt("id"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        o.setUpdatedAt(rs.getTimestamp("updated_at"));
        return o;
    }
}
