package com.shopsphere.dao;

import com.shopsphere.model.OrderItem;

import java.sql.Connection;
import java.util.List;

/**
 * Data Access Object interface for OrderItem entities.
 */
public interface OrderItemDAO {
    List<OrderItem> findByOrderId(int orderId);
    void insertBatch(Connection conn, List<OrderItem> items);
}
