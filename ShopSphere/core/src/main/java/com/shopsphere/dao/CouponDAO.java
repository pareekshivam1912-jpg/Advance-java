package com.shopsphere.dao;

import com.shopsphere.model.Coupon;

import java.sql.Connection;
import java.util.List;

/**
 * Data Access Object interface for Coupon entities.
 */
public interface CouponDAO {
    Coupon findById(int id);
    Coupon findByCode(String code);
    List<Coupon> findAll();
    int insert(Coupon coupon);
    boolean update(Coupon coupon);
    boolean updateStatus(int id, String status);
    boolean isCouponUsedByUser(int couponId, int userId);
    boolean recordCouponUsage(Connection conn, int couponId, int userId, int orderId);
}
