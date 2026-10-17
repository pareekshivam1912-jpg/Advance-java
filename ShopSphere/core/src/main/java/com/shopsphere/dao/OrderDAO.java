package com.shopsphere.dao;

import com.shopsphere.model.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

/**
 * Data Access Object interface for Order entities.
 */
public interface OrderDAO {
    Order findById(int id);
    List<Order> findByUserId(int userId);
    List<Order> findAll();
    List<Order> findByStatus(String status);
    int insert(Connection conn, Order order);
    boolean updateStatus(int id, String orderStatus);
    boolean updatePaymentStatus(int id, String paymentStatus);
    int countAll();
    BigDecimal getTotalSales();
}
