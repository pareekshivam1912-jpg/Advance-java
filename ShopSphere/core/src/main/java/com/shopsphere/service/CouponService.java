package com.shopsphere.service;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Coupon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Service managing promotional coupon validation, discount computation, and configuration.
 */
public class CouponService {

    private final CouponDAO couponDAO;

    public CouponService(CouponDAO couponDAO) {
        this.couponDAO = couponDAO;
    }

    public List<Coupon> getAllCoupons() {
        return couponDAO.findAll();
    }

    public Coupon getCouponById(int id) {
        return couponDAO.findById(id);
    }

    public Coupon getCouponByCode(String code) {
        if (code == null) return null;
        return couponDAO.findByCode(code.trim().toUpperCase());
    }

    public Coupon addCoupon(Coupon coupon) {
        if (coupon.getCode() == null || coupon.getCode().trim().isEmpty()) {
            throw new ShopSphereException("Coupon code is required.");
        }
        coupon.setCode(coupon.getCode().trim().toUpperCase());
        if (couponDAO.findByCode(coupon.getCode()) != null) {
            throw new ShopSphereException("Coupon code already exists.");
        }
        int id = couponDAO.insert(coupon);
        coupon.setId(id);
        return coupon;
    }

    public boolean updateCoupon(Coupon coupon) {
        return couponDAO.update(coupon);
    }

    public boolean toggleCouponStatus(int id, String status) {
        return couponDAO.updateStatus(id, status);
    }

    /**
     * Validates if a coupon is eligible for a user and calculates the discount amount.
     *
     * @param code coupon promo code
     * @param userId authenticated user ID
     * @param subtotal cart subtotal before coupon
     * @return calculated discount amount
     */
    public BigDecimal validateAndCalculateDiscount(String code, int userId, BigDecimal subtotal) {
        if (code == null || code.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        Coupon coupon = couponDAO.findByCode(code.trim().toUpperCase());
        if (coupon == null) {
            throw new ShopSphereException("Invalid coupon code: " + code);
        }
        if (!coupon.isActive()) {
            throw new ShopSphereException("Coupon '" + code + "' has expired or is inactive.");
        }
        if (subtotal == null || subtotal.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new ShopSphereException("Coupon '" + code + "' requires a minimum order amount of ₹" + coupon.getMinOrderAmount() + ".");
        }
        if (couponDAO.isCouponUsedByUser(coupon.getId(), userId)) {
            throw new ShopSphereException("You have already used coupon '" + code + "'. Only one use per customer is allowed.");
        }

        return calculateDiscountAmount(coupon, subtotal);
    }

    /**
     * Calculates discount without user-check (useful for pre-checkout preview and unit tests).
     */
    public BigDecimal calculateDiscountAmount(Coupon coupon, BigDecimal subtotal) {
        if (coupon == null || subtotal == null || subtotal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;
        if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
            BigDecimal percentage = coupon.getDiscountValue().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            discount = subtotal.multiply(percentage).setScale(2, RoundingMode.HALF_UP);
            if (coupon.getMaxDiscountAmount() != null && discount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
                discount = coupon.getMaxDiscountAmount();
            }
        } else { // FLAT
            discount = coupon.getDiscountValue().setScale(2, RoundingMode.HALF_UP);
        }

        // Discount cannot exceed subtotal
        if (discount.compareTo(subtotal) > 0) {
            discount = subtotal;
        }
        return discount;
    }
}
