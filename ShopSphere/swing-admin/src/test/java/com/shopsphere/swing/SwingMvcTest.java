package com.shopsphere.swing;

import com.shopsphere.model.Category;
import com.shopsphere.model.Order;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import com.shopsphere.swing.model.CategoryTableModel;
import com.shopsphere.swing.model.CustomerTableModel;
import com.shopsphere.swing.model.InventoryTableModel;
import com.shopsphere.swing.model.OrderTableModel;
import com.shopsphere.swing.model.ProductTableModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test suite verifying Swing MVC TableModels and data binding.
 */
public class SwingMvcTest {

    @Test
    void testProductTableModel() {
        Product p = new Product();
        p.setId(10);
        p.setName("Wireless Mouse");
        p.setBrand("Logitech");
        p.setCategoryName("Electronics");
        p.setPrice(new BigDecimal("1299.00"));
        p.setDiscountPercentage(new BigDecimal("10.00"));
        p.setStock(40);
        p.setStatus("ACTIVE");

        ProductTableModel model = new ProductTableModel(List.of(p));
        assertEquals(1, model.getRowCount());
        assertEquals(8, model.getColumnCount());
        assertEquals("Name", model.getColumnName(1));
        assertEquals("Wireless Mouse", model.getValueAt(0, 1));
        assertEquals(40, model.getValueAt(0, 6));
        assertFalse(model.isCellEditable(0, 0));
        assertNotNull(model.getProductAt(0));
    }

    @Test
    void testCategoryTableModel() {
        Category c = new Category();
        c.setId(2);
        c.setName("Laptops");
        c.setDescription("High performance laptops");
        c.setStatus("ACTIVE");

        CategoryTableModel model = new CategoryTableModel(List.of(c));
        assertEquals(1, model.getRowCount());
        assertEquals(4, model.getColumnCount());
        assertEquals("Laptops", model.getValueAt(0, 1));
    }

    @Test
    void testCustomerTableModel() {
        User u = new User();
        u.setId(5);
        u.setName("Test Customer");
        u.setEmail("customer@test.com");
        u.setPhone("9876543210");
        u.setRole("CUSTOMER");
        u.setStatus("ACTIVE");

        CustomerTableModel model = new CustomerTableModel(List.of(u));
        assertEquals(1, model.getRowCount());
        assertEquals(6, model.getColumnCount());
        assertEquals("customer@test.com", model.getValueAt(0, 2));
    }

    @Test
    void testOrderTableModel() {
        Order o = new Order();
        o.setId(101);
        o.setUserId(2);
        o.setUserFullName("Rahul Sharma");
        o.setTotalAmount(new BigDecimal("4999.00"));
        o.setOrderStatus("CONFIRMED");
        o.setPaymentStatus("PAID");
        o.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        OrderTableModel model = new OrderTableModel(List.of(o));
        assertEquals(1, model.getRowCount());
        assertEquals(7, model.getColumnCount());
        assertEquals("CONFIRMED", model.getValueAt(0, 4));
    }

    @Test
    void testInventoryTableModel() {
        Product p = new Product();
        p.setId(3);
        p.setName("Gaming Keyboard");
        p.setBrand("Razer");
        p.setCategoryName("Electronics");
        p.setPrice(new BigDecimal("4999.00"));
        p.setStock(3);

        InventoryTableModel model = new InventoryTableModel(List.of(p));
        assertEquals(1, model.getRowCount());
        assertEquals(7, model.getColumnCount());
        assertEquals(3, model.getValueAt(0, 4));
        assertEquals("VERY LOW", model.getValueAt(0, 6));
    }
}
