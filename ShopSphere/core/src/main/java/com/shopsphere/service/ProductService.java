package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service managing product catalog, searching, filtering, and inventory updates.
 */
public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product getProductById(int id) {
        return productDAO.findById(id);
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public List<Product> getFeaturedProducts(int limit) {
        return productDAO.findFeatured(limit);
    }

    public List<Product> getRecentProducts(int limit) {
        return productDAO.findRecent(limit);
    }

    public List<Product> getRelatedProducts(int categoryId, int limit) {
        return productDAO.findByCategory(categoryId, limit);
    }

    public List<Product> searchProducts(String keyword, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, int page, int pageSize) {
        return productDAO.search(keyword, categoryId, minPrice, maxPrice, sortBy, page, pageSize);
    }

    public int getSearchCount(String keyword, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        return productDAO.countSearch(keyword, categoryId, minPrice, maxPrice);
    }

    public List<Product> getLowStockProducts(int threshold) {
        return productDAO.findLowStock(threshold);
    }

    public Product addProduct(Product product) {
        validateProduct(product);
        int id = productDAO.insert(product);
        product.setId(id);
        return product;
    }

    public boolean updateProduct(Product product) {
        validateProduct(product);
        return productDAO.update(product);
    }

    public boolean deleteProduct(int id) {
        return productDAO.delete(id);
    }

    public boolean updateStock(int productId, int newStock) {
        if (newStock < 0) {
            throw new ShopSphereException("Stock cannot be negative.");
        }
        return productDAO.updateStock(productId, newStock);
    }

    private void validateProduct(Product p) {
        if (p.getName() == null || p.getName().trim().isEmpty()) {
            throw new ShopSphereException("Product name is required.");
        }
        if (p.getCategoryId() <= 0) {
            throw new ShopSphereException("Valid category is required.");
        }
        if (p.getPrice() == null || p.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ShopSphereException("Price must be non-negative.");
        }
        if (p.getStock() < 0) {
            throw new ShopSphereException("Stock must be non-negative.");
        }
    }
}
