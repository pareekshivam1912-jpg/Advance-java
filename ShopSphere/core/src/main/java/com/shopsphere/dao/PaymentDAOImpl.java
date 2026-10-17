package com.shopsphere.dao;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Payment;
import com.shopsphere.util.DBUtil;

import java.sql.*;

/**
 * JDBC implementation of PaymentDAO using PreparedStatements and try-with-resources.
 */
public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public Payment findByOrderId(int orderId) {
        String sql = "SELECT id, order_id, payment_method, transaction_reference, amount, payment_status, payment_date FROM payments WHERE order_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Payment(
                            rs.getInt("id"),
                            rs.getInt("order_id"),
                            rs.getString("payment_method"),
                            rs.getString("transaction_reference"),
                            rs.getBigDecimal("amount"),
                            rs.getString("payment_status"),
                            rs.getTimestamp("payment_date")
                    );
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding payment for order: " + orderId, e);
        }
        return null;
    }

    @Override
    public int insert(Connection conn, Payment payment) {
        String sql = "INSERT INTO payments (order_id, payment_method, transaction_reference, amount, payment_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getTransactionReference());
            ps.setBigDecimal(4, payment.getAmount());
            ps.setString(5, payment.getPaymentStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        payment.setId(generatedId);
                        return generatedId;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error inserting payment record", e);
        }
        return -1;
    }

    @Override
    public boolean updateStatus(int id, String paymentStatus) {
        String sql = "UPDATE payments SET payment_status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paymentStatus);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating payment status for id: " + id, e);
        }
    }
}
