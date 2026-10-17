package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service managing customer shopping cart operations and stock boundary checks.
 */
public class CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public List<CartItem> getCart(int userId) {
        return cartDAO.findByUserId(userId);
    }

    public void addToCart(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            throw new ShopSphereException("Quantity must be at least 1.");
        }
        Product product = productDAO.findById(productId);
        if (product == null || !"ACTIVE".equalsIgnoreCase(product.getStatus())) {
            throw new ShopSphereException("Product not found or unavailable.");
        }
        if (product.getStock() <= 0) {
            throw new ShopSphereException("Sorry, '" + product.getName() + "' is out of stock.");
        }

        CartItem existing = cartDAO.findByUserAndProduct(userId, productId);
        int currentQty = existing != null ? existing.getQuantity() : 0;
        int totalRequested = currentQty + quantity;

        if (totalRequested > product.getStock()) {
            throw new ShopSphereException("Only " + product.getStock() + " units available in stock. You already have " + currentQty + " in your cart.");
        }

        cartDAO.addItem(userId, productId, quantity);
    }

    public void updateQuantity(int userId, int productId, int newQuantity) {
        if (newQuantity <= 0) {
            cartDAO.removeItem(userId, productId);
            return;
        }
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new ShopSphereException("Product not found.");
        }
        if (newQuantity > product.getStock()) {
            throw new ShopSphereException("Cannot select " + newQuantity + " units. Only " + product.getStock() + " units available in stock.");
        }
        cartDAO.updateQuantity(userId, productId, newQuantity);
    }

    public void removeFromCart(int userId, int productId) {
        cartDAO.removeItem(userId, productId);
    }

    public void clearCart(int userId) {
        cartDAO.clearCart(userId);
    }

    public int getCartCount(int userId) {
        return cartDAO.getCartCount(userId);
    }

    public BigDecimal calculateSubtotal(List<CartItem> items) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : items) {
            subtotal = subtotal.add(item.getLineTotal());
        }
        return subtotal;
    }
}
