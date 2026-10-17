package com.shopsphere.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain entity representing a customer order.
 * Implements Serializable.
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private int addressId;
    private Integer couponId;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
    private String orderStatus; // PLACED, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    private String paymentMethod; // COD, CARD, UPI
    private String paymentStatus; // PENDING, PAID, FAILED
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Attached relation entities for UI/Invoice rendering
    private List<OrderItem> items = new ArrayList<>();
    private Address address;
    private User user;
    private String userFullName;
    private Payment payment;
    private Coupon coupon;

    public Order() {
        this.subtotal = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.shippingFee = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
        this.orderStatus = "PLACED";
        this.paymentStatus = "PENDING";
    }

    public Order(int userId, int addressId, Integer couponId, BigDecimal subtotal,
                 BigDecimal discountAmount, BigDecimal shippingFee, BigDecimal totalAmount,
                 String orderStatus, String paymentMethod, String paymentStatus) {
        this.userId = userId;
        this.addressId = addressId;
        this.couponId = couponId;
        this.subtotal = subtotal != null ? subtotal : BigDecimal.ZERO;
        this.discountAmount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        this.shippingFee = shippingFee != null ? shippingFee : BigDecimal.ZERO;
        this.totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        this.orderStatus = orderStatus != null ? orderStatus : "PLACED";
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus != null ? paymentStatus : "PENDING";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserFullName() {
        if (userFullName != null && !userFullName.isEmpty()) {
            return userFullName;
        }
        return user != null && user.getName() != null ? user.getName() : "Customer #" + userId;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public boolean canCancel() {
        return "PLACED".equalsIgnoreCase(this.orderStatus) || "CONFIRMED".equalsIgnoreCase(this.orderStatus);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
