package com.shopsphere.dao;

import com.shopsphere.model.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

/**
 * Data Access Object interface for Product entities.
 */
public interface ProductDAO {
    Product findById(int id);
    List<Product> findAll();
    List<Product> findFeatured(int limit);
    List<Product> findRecent(int limit);
    List<Product> findByCategory(int categoryId, int limit);
    List<Product> search(String keyword, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, int page, int pageSize);
    int countSearch(String keyword, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findLowStock(int threshold);
    int insert(Product product);
    boolean update(Product product);
    boolean delete(int id);
    boolean updateStock(int productId, int newStock);
    boolean updateStock(Connection conn, int productId, int quantityDelta);
    boolean updateRating(int productId, BigDecimal newRating);
}
