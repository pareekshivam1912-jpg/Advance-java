package com.shopsphere.service;

import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.WishlistItem;

import java.util.List;

/**
 * Service managing customer wishlist and direct transitions to shopping cart.
 */
public class WishlistService {

    private final WishlistDAO wishlistDAO;
    private final CartService cartService;

    public WishlistService(WishlistDAO wishlistDAO, CartService cartService) {
        this.wishlistDAO = wishlistDAO;
        this.cartService = cartService;
    }

    public List<WishlistItem> getWishlist(int userId) {
        return wishlistDAO.findByUserId(userId);
    }

    public void addToWishlist(int userId, int productId) {
        wishlistDAO.addItem(userId, productId);
    }

    public void removeFromWishlist(int userId, int productId) {
        wishlistDAO.removeItem(userId, productId);
    }

    public boolean isInWishlist(int userId, int productId) {
        return wishlistDAO.isInWishlist(userId, productId);
    }

    public int getWishlistCount(int userId) {
        return wishlistDAO.getWishlistCount(userId);
    }

    public void moveToCart(int userId, int productId) {
        cartService.addToCart(userId, productId, 1);
        wishlistDAO.removeItem(userId, productId);
    }
}
