package com.shopsphere.dao;

import com.shopsphere.model.Payment;

import java.sql.Connection;

/**
 * Data Access Object interface for Payment entities.
 */
public interface PaymentDAO {
    Payment findByOrderId(int orderId);
    int insert(Connection conn, Payment payment);
    boolean updateStatus(int id, String paymentStatus);
}
