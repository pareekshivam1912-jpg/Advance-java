package com.shopsphere.service;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests verifying coupon validation and discount computation logic.
 */
public class CouponServiceTest {

    private CouponService couponService;

    // Fake in-memory DAO for isolated unit testing
    private static class FakeCouponDAO implements CouponDAO {
        private Coupon coupon;
        private boolean alreadyUsed = false;

        public void setCoupon(Coupon coupon) { this.coupon = coupon; }
        public void setAlreadyUsed(boolean used) { this.alreadyUsed = used; }

        @Override public Coupon findById(int id) { return coupon; }
        @Override public Coupon findByCode(String code) {
            return (coupon != null && coupon.getCode().equalsIgnoreCase(code)) ? coupon : null;
        }
        @Override public java.util.List<Coupon> findAll() { return java.util.List.of(coupon); }
        @Override public int insert(Coupon coupon) { return 1; }
        @Override public boolean update(Coupon coupon) { return true; }
        @Override public boolean updateStatus(int id, String status) { return true; }
        @Override public boolean isCouponUsedByUser(int couponId, int userId) { return alreadyUsed; }
        @Override public boolean recordCouponUsage(java.sql.Connection conn, int couponId, int userId, int orderId) { return true; }
    }

    private FakeCouponDAO fakeCouponDAO;

    @BeforeEach
    public void setUp() {
        fakeCouponDAO = new FakeCouponDAO();
        couponService = new CouponService(fakeCouponDAO);
    }

    @Test
    public void testPercentageDiscountCalculation() {
        Coupon coupon = new Coupon("SAVE10", "PERCENTAGE", new BigDecimal("10.00"),
                new BigDecimal("500.00"), new BigDecimal("300.00"),
                Date.valueOf(LocalDate.now().minusDays(1)), Date.valueOf(LocalDate.now().plusDays(10)), "ACTIVE");

        BigDecimal discount = couponService.calculateDiscountAmount(coupon, new BigDecimal("2000.00"));
        assertEquals(new BigDecimal("200.00"), discount);
    }

    @Test
    public void testPercentageDiscountCap() {
        Coupon coupon = new Coupon("SUPER20", "PERCENTAGE", new BigDecimal("20.00"),
                new BigDecimal("1000.00"), new BigDecimal("500.00"), // Cap is 500
                Date.valueOf(LocalDate.now().minusDays(1)), Date.valueOf(LocalDate.now().plusDays(10)), "ACTIVE");

        // 20% of 5000 is 1000, but cap is 500
        BigDecimal discount = couponService.calculateDiscountAmount(coupon, new BigDecimal("5000.00"));
        assertEquals(new BigDecimal("500.00"), discount);
    }

    @Test
    public void testFlatDiscountCalculation() {
        Coupon coupon = new Coupon("FLAT250", "FLAT", new BigDecimal("250.00"),
                new BigDecimal("1000.00"), null,
                Date.valueOf(LocalDate.now().minusDays(1)), Date.valueOf(LocalDate.now().plusDays(10)), "ACTIVE");

        BigDecimal discount = couponService.calculateDiscountAmount(coupon, new BigDecimal("1500.00"));
        assertEquals(new BigDecimal("250.00"), discount);
    }

    @Test
    public void testFlatDiscountCannotExceedSubtotal() {
        Coupon coupon = new Coupon("FLAT500", "FLAT", new BigDecimal("500.00"),
                BigDecimal.ZERO, null,
                Date.valueOf(LocalDate.now().minusDays(1)), Date.valueOf(LocalDate.now().plusDays(10)), "ACTIVE");

        // If subtotal is 300, discount cannot exceed 300
        BigDecimal discount = couponService.calculateDiscountAmount(coupon, new BigDecimal("300.00"));
        assertEquals(new BigDecimal("300.00"), discount);
    }

    @Test
    public void testMinimumOrderAmountEnforced() {
        Coupon coupon = new Coupon("MIN1000", "FLAT", new BigDecimal("100.00"),
                new BigDecimal("1000.00"), null,
                Date.valueOf(LocalDate.now().minusDays(1)), Date.valueOf(LocalDate.now().plusDays(10)), "ACTIVE");
        fakeCouponDAO.setCoupon(coupon);

        assertThrows(ShopSphereException.class, () ->
                couponService.validateAndCalculateDiscount("MIN1000", 1, new BigDecimal("800.00")),
                "Should reject coupon when subtotal is below minimum order amount");
    }

    @Test
    public void testExpiredCouponRejected() {
        Coupon expired = new Coupon("EXPIRED", "FLAT", new BigDecimal("100.00"),
                BigDecimal.ZERO, null,
                Date.valueOf(LocalDate.now().minusDays(20)), Date.valueOf(LocalDate.now().minusDays(5)), "ACTIVE");
        fakeCouponDAO.setCoupon(expired);

        assertThrows(ShopSphereException.class, () ->
                couponService.validateAndCalculateDiscount("EXPIRED", 1, new BigDecimal("1500.00")),
                "Should reject expired coupon");
    }

    @Test
    public void testSingleUsePerCustomerEnforced() {
        Coupon coupon = new Coupon("ONCE", "FLAT", new BigDecimal("100.00"),
                BigDecimal.ZERO, null,
                Date.valueOf(LocalDate.now().minusDays(1)), Date.valueOf(LocalDate.now().plusDays(10)), "ACTIVE");
        fakeCouponDAO.setCoupon(coupon);
        fakeCouponDAO.setAlreadyUsed(true);

        assertThrows(ShopSphereException.class, () ->
                couponService.validateAndCalculateDiscount("ONCE", 1, new BigDecimal("1500.00")),
                "Should reject coupon if user already used it");
    }
}
