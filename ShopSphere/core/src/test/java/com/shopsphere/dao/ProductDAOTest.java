package com.shopsphere.dao;

import com.shopsphere.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit and integration tests for ProductDAO against MySQL.
 */
public class ProductDAOTest {

    private static ProductDAO productDAO;

    @BeforeAll
    public static void setUp() {
        productDAO = new ProductDAOImpl();
    }

    @Test
    public void testFindById() {
        Product p = productDAO.findById(1);
        assertNotNull(p, "Product 1 should exist");
        assertEquals("Sony", p.getBrand());
        assertEquals("Electronics & Audio", p.getCategoryName());
        assertTrue(p.getStock() > 0);
    }

    @Test
    public void testDiscountCalculation() {
        Product p = productDAO.findById(1);
        assertNotNull(p);
        // Price: 29990.00, Discount: 10.00% -> 26991.00
        BigDecimal discounted = p.getDiscountedPrice();
        assertEquals(new BigDecimal("26991.00"), discounted);
    }

    @Test
    public void testSearchKeyword() {
        List<Product> results = productDAO.search("Headphones", null, null, null, "price_asc", 1, 10);
        assertNotNull(results);
        assertFalse(results.isEmpty(), "Search for 'Headphones' should return seeded products");
        boolean hasSony = results.stream().anyMatch(prod -> "Sony".equalsIgnoreCase(prod.getBrand()));
        assertTrue(hasSony, "Results should contain Sony headphones");
    }

    @Test
    public void testLowStockFilter() {
        List<Product> lowStock = productDAO.findLowStock(5);
        assertNotNull(lowStock);
        for (Product prod : lowStock) {
            assertTrue(prod.getStock() <= 5, "Product stock must be <= threshold of 5");
        }
    }

    @Test
    public void testCountSearch() {
        int count = productDAO.countSearch(null, null, null, null);
        assertTrue(count >= 30, "Total active products count should be at least 30");
    }
}
