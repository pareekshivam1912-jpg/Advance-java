package com.shopsphere.dao;

import com.shopsphere.model.CartItem;

import java.sql.Connection;
import java.util.List;

/**
 * Data Access Object interface for CartItem entities.
 */
public interface CartDAO {
    List<CartItem> findByUserId(int userId);
    CartItem findByUserAndProduct(int userId, int productId);
    boolean addItem(int userId, int productId, int quantity);
    boolean updateQuantity(int userId, int productId, int quantity);
    boolean removeItem(int userId, int productId);
    boolean clearCart(int userId);
    boolean clearCart(Connection conn, int userId);
    int getCartCount(int userId);
}
