package com.shopsphere.swing.controller;

import com.shopsphere.model.Order;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.ServiceFactory;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.function.Consumer;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Order Controller.
 */
public class OrderController {

    private final OrderService orderService;

    public OrderController() {
        this.orderService = ServiceFactory.getOrderService();
    }

    public void loadOrders(Consumer<List<Order>> callback, Consumer<String> onError) {
        new SwingWorker<List<Order>, Void>() {
            @Override
            protected List<Order> doInBackground() {
                return orderService.getAllOrders();
            }

            @Override
            protected void done() {
                try {
                    callback.accept(get());
                } catch (Exception e) {
                    onError.accept("Error loading orders: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void updateOrderStatus(int orderId, String newStatus, Runnable onSuccess, Consumer<String> onError) {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return orderService.updateOrderStatus(orderId, newStatus);
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        onSuccess.run();
                    } else {
                        onError.accept("Failed to update order status.");
                    }
                } catch (Exception e) {
                    onError.accept("Error: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void getOrderDetails(int orderId, Consumer<Order> callback, Consumer<String> onError) {
        new SwingWorker<Order, Void>() {
            @Override
            protected Order doInBackground() {
                return orderService.getOrderDetails(orderId);
            }

            @Override
            protected void done() {
                try {
                    callback.accept(get());
                } catch (Exception e) {
                    onError.accept("Error loading order details: " + e.getMessage());
                }
            }
        }.execute();
    }
}
