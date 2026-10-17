package com.shopsphere.service;

import com.shopsphere.dao.*;
import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.*;
import com.shopsphere.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service managing checkout calculation and atomic order placement transaction.
 * Guarantees that order totals and stock checks are strictly executed on the server.
 */
public class CheckoutService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final PaymentDAO paymentDAO;
    private final CouponDAO couponDAO;
    private final CouponService couponService;

    public CheckoutService(CartDAO cartDAO, ProductDAO productDAO, OrderDAO orderDAO,
                           OrderItemDAO orderItemDAO, PaymentDAO paymentDAO,
                           CouponDAO couponDAO, CouponService couponService) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.paymentDAO = paymentDAO;
        this.couponDAO = couponDAO;
        this.couponService = couponService;
    }

    /**
     * DTO representing computed order breakdown before placement.
     */
    public static class OrderSummary {
        private final BigDecimal subtotal;
        private final BigDecimal discountAmount;
        private final BigDecimal shippingFee;
        private final BigDecimal totalAmount;
        private final Coupon appliedCoupon;
        private final List<CartItem> items;

        public OrderSummary(BigDecimal subtotal, BigDecimal discountAmount, BigDecimal shippingFee,
                            BigDecimal totalAmount, Coupon appliedCoupon, List<CartItem> items) {
            this.subtotal = subtotal;
            this.discountAmount = discountAmount;
            this.shippingFee = shippingFee;
            this.totalAmount = totalAmount;
            this.appliedCoupon = appliedCoupon;
            this.items = items;
        }

        public BigDecimal getSubtotal() { return subtotal; }
        public BigDecimal getDiscountAmount() { return discountAmount; }
        public BigDecimal getShippingFee() { return shippingFee; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public Coupon getAppliedCoupon() { return appliedCoupon; }
        public List<CartItem> getItems() { return items; }
    }

    /**
     * Recomputes the entire checkout summary strictly from database state.
     */
    public OrderSummary computeSummary(int userId, String couponCode) {
        List<CartItem> items = cartDAO.findByUserId(userId);
        if (items.isEmpty()) {
            throw new ShopSphereException("Your cart is empty.");
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : items) {
            Product dbProd = productDAO.findById(item.getProductId());
            if (dbProd == null || !"ACTIVE".equalsIgnoreCase(dbProd.getStatus())) {
                throw new ShopSphereException("A product in your cart is no longer available.");
            }
            if (dbProd.getStock() < item.getQuantity()) {
                throw new ShopSphereException("Product '" + dbProd.getName() + "' has only " + dbProd.getStock() + " units available.");
            }
            item.setProduct(dbProd);
            subtotal = subtotal.add(dbProd.getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        Coupon coupon = null;
        BigDecimal couponDiscount = BigDecimal.ZERO;
        if (couponCode != null && !couponCode.trim().isEmpty()) {
            coupon = couponService.getCouponByCode(couponCode);
            couponDiscount = couponService.validateAndCalculateDiscount(couponCode, userId, subtotal);
        }

        // Free shipping on orders >= 1000, otherwise flat 50
        BigDecimal shipping = subtotal.compareTo(new BigDecimal("1000")) >= 0 ? BigDecimal.ZERO : new BigDecimal("50.00");
        BigDecimal total = subtotal.subtract(couponDiscount).add(shipping);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        return new OrderSummary(subtotal, couponDiscount, shipping, total, coupon, items);
    }

    /**
     * Executes the order placement in a single atomic database transaction.
     *
     * @param userId customer ID
     * @param addressId delivery address ID
     * @param couponCode optional coupon promo code
     * @param paymentMethod COD, CARD, or UPI
     * @return newly created Order entity
     */
    public Order placeOrder(int userId, int addressId, String couponCode, String paymentMethod) {
        OrderSummary summary = computeSummary(userId, couponCode);

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // Begin atomic transaction

            // 1. Create Order entity
            Integer couponId = summary.getAppliedCoupon() != null ? summary.getAppliedCoupon().getId() : null;
            String paymentStatus = "COD".equalsIgnoreCase(paymentMethod) ? "PENDING" : "PAID";
            String orderStatus = "PLACED";

            Order order = new Order(
                    userId,
                    addressId,
                    couponId,
                    summary.getSubtotal(),
                    summary.getDiscountAmount(),
                    summary.getShippingFee(),
                    summary.getTotalAmount(),
                    orderStatus,
                    paymentMethod,
                    paymentStatus
            );

            int orderId = orderDAO.insert(conn, order);
            if (orderId <= 0) {
                throw new ShopSphereException("Failed to generate order record.");
            }

            // 2. Prepare and batch insert OrderItems
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem ci : summary.getItems()) {
                OrderItem oi = new OrderItem(
                        orderId,
                        ci.getProductId(),
                        ci.getProduct().getDiscountedPrice(),
                        ci.getQuantity(),
                        ci.getLineTotal()
                );
                orderItems.add(oi);

                // 3. Decrement product stock in same transaction
                boolean stockReduced = productDAO.updateStock(conn, ci.getProductId(), -ci.getQuantity());
                if (!stockReduced) {
                    throw new ShopSphereException("Insufficient stock for product '" + ci.getProduct().getName() + "'.");
                }
            }
            orderItemDAO.insertBatch(conn, orderItems);
            order.setItems(orderItems);

            // 4. Record coupon usage if coupon applied
            if (couponId != null) {
                couponDAO.recordCouponUsage(conn, couponId, userId, orderId);
            }

            // 5. Create Payment record
            String txnRef = "TXN_" + paymentMethod + "_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Payment payment = new Payment(orderId, paymentMethod, txnRef, summary.getTotalAmount(), paymentStatus);
            paymentDAO.insert(conn, payment);
            order.setPayment(payment);

            // 6. Clear shopping cart
            cartDAO.clearCart(conn, userId);

            // Commit atomic transaction
            conn.commit();
            return order;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    // Log or suppress
                }
            }
            if (e instanceof ShopSphereException) {
                throw (ShopSphereException) e;
            }
            throw new ShopSphereException("Order placement failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    // Suppress
                }
            }
        }
    }
}
