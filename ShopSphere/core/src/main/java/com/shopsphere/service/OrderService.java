package com.shopsphere.service;

import com.shopsphere.dao.*;
import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;

import java.util.List;

/**
 * Service managing customer orders, tracking history, admin status updates, and cancellations.
 */
public class OrderService {

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final AddressDAO addressDAO;
    private final PaymentDAO paymentDAO;
    private final CouponDAO couponDAO;
    private final ProductDAO productDAO;

    public OrderService(OrderDAO orderDAO, OrderItemDAO orderItemDAO, AddressDAO addressDAO,
                        PaymentDAO paymentDAO, CouponDAO couponDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.addressDAO = addressDAO;
        this.paymentDAO = paymentDAO;
        this.couponDAO = couponDAO;
        this.productDAO = productDAO;
    }

    public Order getOrderDetails(int orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            return null;
        }
        order.setItems(orderItemDAO.findByOrderId(orderId));
        order.setAddress(addressDAO.findById(order.getAddressId()));
        order.setPayment(paymentDAO.findByOrderId(orderId));
        if (order.getCouponId() != null) {
            order.setCoupon(couponDAO.findById(order.getCouponId()));
        }
        return order;
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> list = orderDAO.findByUserId(userId);
        for (Order o : list) {
            o.setItems(orderItemDAO.findByOrderId(o.getId()));
            o.setPayment(paymentDAO.findByOrderId(o.getId()));
        }
        return list;
    }

    public List<Order> getAllOrders() {
        List<Order> list = orderDAO.findAll();
        for (Order o : list) {
            o.setPayment(paymentDAO.findByOrderId(o.getId()));
        }
        return list;
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderDAO.findByStatus(status);
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        return orderDAO.updateStatus(orderId, newStatus);
    }

    public boolean updatePaymentStatus(int orderId, String newStatus) {
        return orderDAO.updatePaymentStatus(orderId, newStatus);
    }

    public void cancelOrder(int orderId, int userId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new ShopSphereException("Order not found.");
        }
        if (order.getUserId() != userId) {
            throw new ShopSphereException("Unauthorized access to order.");
        }
        if (!order.canCancel()) {
            throw new ShopSphereException("Order cannot be cancelled as it is already " + order.getOrderStatus() + ".");
        }

        // Restore product stock
        List<OrderItem> items = orderItemDAO.findByOrderId(orderId);
        for (OrderItem item : items) {
            productDAO.updateStock(null, item.getProductId(), item.getQuantity());
        }

        orderDAO.updateStatus(orderId, "CANCELLED");
    }

    public int getTotalOrdersCount() {
        return orderDAO.countAll();
    }
}
