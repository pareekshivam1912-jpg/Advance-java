package com.shopsphere.dao;

import com.shopsphere.model.WishlistItem;

import java.util.List;

/**
 * Data Access Object interface for WishlistItem entities.
 */
public interface WishlistDAO {
    List<WishlistItem> findByUserId(int userId);
    boolean addItem(int userId, int productId);
    boolean removeItem(int userId, int productId);
    boolean isInWishlist(int userId, int productId);
    int getWishlistCount(int userId);
}
