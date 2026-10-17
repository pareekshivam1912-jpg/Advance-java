package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

import java.util.List;

/**
 * Service managing warehouse inventory levels and stock replenishments.
 * Reused by local web controllers, Swing admin, and RMI/Socket backends.
 */
public class LocalInventoryService {

    private final ProductDAO productDAO;

    public LocalInventoryService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product getProduct(int productId) {
        return productDAO.findById(productId);
    }

    public int getStock(int productId) {
        Product p = productDAO.findById(productId);
        return p != null ? p.getStock() : 0;
    }

    public boolean updateStock(int productId, int newStock) {
        return productDAO.updateStock(productId, newStock);
    }

    public List<Product> getLowStockProducts(int threshold) {
        return productDAO.findLowStock(threshold);
    }
}
