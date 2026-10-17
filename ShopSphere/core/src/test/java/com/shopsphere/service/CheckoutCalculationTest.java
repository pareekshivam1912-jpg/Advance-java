package com.shopsphere.service;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests verifying price discount recalculation, line items and checkout total formulas.
 */
public class CheckoutCalculationTest {

    @Test
    public void testDiscountedProductPrice() {
        Product p = new Product();
        p.setPrice(new BigDecimal("1000.00"));
        p.setDiscountPercentage(new BigDecimal("25.00"));

        BigDecimal finalPrice = p.getDiscountedPrice();
        assertEquals(new BigDecimal("750.00"), finalPrice);
    }

    @Test
    public void testCartLineTotalCalculation() {
        Product p = new Product();
        p.setPrice(new BigDecimal("500.00"));
        p.setDiscountPercentage(new BigDecimal("10.00")); // selling price 450.00

        CartItem item = new CartItem(1, 101, 3);
        item.setProduct(p);

        // 450.00 * 3 = 1350.00
        BigDecimal lineTotal = item.getLineTotal();
        assertEquals(new BigDecimal("1350.00"), lineTotal);
    }

    @Test
    public void testCartSubtotalAccumulation() {
        List<CartItem> items = new ArrayList<>();

        Product p1 = new Product();
        p1.setPrice(new BigDecimal("200.00"));
        p1.setDiscountPercentage(BigDecimal.ZERO);
        CartItem item1 = new CartItem(1, 1, 2); // 400.00
        item1.setProduct(p1);
        items.add(item1);

        Product p2 = new Product();
        p2.setPrice(new BigDecimal("1000.00"));
        p2.setDiscountPercentage(new BigDecimal("20.00")); // 800.00
        CartItem item2 = new CartItem(1, 2, 1); // 800.00
        item2.setProduct(p2);
        items.add(item2);

        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem ci : items) {
            subtotal = subtotal.add(ci.getLineTotal());
        }
        assertEquals(new BigDecimal("1200.00"), subtotal);
    }
}
