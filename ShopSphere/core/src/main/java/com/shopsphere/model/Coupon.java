package com.shopsphere.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Domain entity representing promotional discount coupons.
 */
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String code;
    private String discountType; // 'PERCENTAGE' or 'FLAT'
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;
    private Date startDate;
    private Date expiryDate;
    private String status; // 'ACTIVE' or 'INACTIVE'
    private Timestamp createdAt;

    public Coupon() {
        this.discountValue = BigDecimal.ZERO;
        this.minOrderAmount = BigDecimal.ZERO;
        this.status = "ACTIVE";
    }

    public Coupon(String code, String discountType, BigDecimal discountValue,
                  BigDecimal minOrderAmount, BigDecimal maxDiscountAmount,
                  Date startDate, Date expiryDate, String status) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount != null ? minOrderAmount : BigDecimal.ZERO;
        this.maxDiscountAmount = maxDiscountAmount;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.status = status != null ? status : "ACTIVE";
    }

    public Coupon(int id, String code, String discountType, BigDecimal discountValue,
                  BigDecimal minOrderAmount, BigDecimal maxDiscountAmount,
                  Date startDate, Date expiryDate, String status, Timestamp createdAt) {
        this.id = id;
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        if (!"ACTIVE".equalsIgnoreCase(this.status)) {
            return false;
        }
        LocalDate now = LocalDate.now();
        if (startDate != null && now.isBefore(startDate.toLocalDate())) {
            return false;
        }
        if (expiryDate != null && now.isAfter(expiryDate.toLocalDate())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", discountType='" + discountType + '\'' +
                ", discountValue=" + discountValue +
                ", status='" + status + '\'' +
                '}';
    }
}
