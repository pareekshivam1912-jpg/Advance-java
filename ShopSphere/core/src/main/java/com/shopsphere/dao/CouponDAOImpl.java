package com.shopsphere.dao;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Coupon;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of CouponDAO using PreparedStatements and try-with-resources.
 */
public class CouponDAOImpl implements CouponDAO {

    @Override
    public Coupon findById(int id) {
        String sql = "SELECT id, code, discount_type, discount_value, min_order_amount, max_discount_amount, start_date, expiry_date, status, created_at FROM coupons WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding coupon by id: " + id, e);
        }
        return null;
    }

    @Override
    public Coupon findByCode(String code) {
        String sql = "SELECT id, code, discount_type, discount_value, min_order_amount, max_discount_amount, start_date, expiry_date, status, created_at FROM coupons WHERE code = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding coupon by code: " + code, e);
        }
        return null;
    }

    @Override
    public List<Coupon> findAll() {
        String sql = "SELECT id, code, discount_type, discount_value, min_order_amount, max_discount_amount, start_date, expiry_date, status, created_at FROM coupons ORDER BY id DESC";
        List<Coupon> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding all coupons", e);
        }
        return list;
    }

    @Override
    public int insert(Coupon coupon) {
        String sql = "INSERT INTO coupons (code, discount_type, discount_value, min_order_amount, max_discount_amount, start_date, expiry_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, coupon.getCode());
            ps.setString(2, coupon.getDiscountType());
            ps.setBigDecimal(3, coupon.getDiscountValue());
            ps.setBigDecimal(4, coupon.getMinOrderAmount());
            if (coupon.getMaxDiscountAmount() != null) {
                ps.setBigDecimal(5, coupon.getMaxDiscountAmount());
            } else {
                ps.setNull(5, Types.DECIMAL);
            }
            ps.setDate(6, coupon.getStartDate());
            ps.setDate(7, coupon.getExpiryDate());
            ps.setString(8, coupon.getStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        coupon.setId(generatedId);
                        return generatedId;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error inserting coupon: " + coupon.getCode(), e);
        }
        return -1;
    }

    @Override
    public boolean update(Coupon coupon) {
        String sql = "UPDATE coupons SET discount_type = ?, discount_value = ?, min_order_amount = ?, max_discount_amount = ?, start_date = ?, expiry_date = ?, status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, coupon.getDiscountType());
            ps.setBigDecimal(2, coupon.getDiscountValue());
            ps.setBigDecimal(3, coupon.getMinOrderAmount());
            if (coupon.getMaxDiscountAmount() != null) {
                ps.setBigDecimal(4, coupon.getMaxDiscountAmount());
            } else {
                ps.setNull(4, Types.DECIMAL);
            }
            ps.setDate(5, coupon.getStartDate());
            ps.setDate(6, coupon.getExpiryDate());
            ps.setString(7, coupon.getStatus());
            ps.setInt(8, coupon.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating coupon: " + coupon.getId(), e);
        }
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE coupons SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating coupon status for id: " + id, e);
        }
    }

    @Override
    public boolean isCouponUsedByUser(int couponId, int userId) {
        String sql = "SELECT id FROM coupon_usage WHERE coupon_id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error checking coupon usage for user: " + userId + ", coupon: " + couponId, e);
        }
    }

    @Override
    public boolean recordCouponUsage(Connection conn, int couponId, int userId, int orderId) {
        String sql = "INSERT INTO coupon_usage (coupon_id, user_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error recording coupon usage for order: " + orderId, e);
        }
    }

    private Coupon mapRow(ResultSet rs) throws SQLException {
        return new Coupon(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("discount_type"),
                rs.getBigDecimal("discount_value"),
                rs.getBigDecimal("min_order_amount"),
                rs.getBigDecimal("max_discount_amount"),
                rs.getDate("start_date"),
                rs.getDate("expiry_date"),
                rs.getString("status"),
                rs.getTimestamp("created_at")
        );
    }
}
