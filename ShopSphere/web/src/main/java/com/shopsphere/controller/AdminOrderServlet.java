package com.shopsphere.controller;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller managing customer orders for administrators:
 * status filtering, status transitions, and payment status updates.
 */
@WebServlet(urlPatterns = {"/admin/orders"})
public class AdminOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.orderService = ServiceFactory.getOrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/admin/orders");
            return;
        }

        String statusFilter = req.getParameter("status");
        List<Order> orders;
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            orders = orderService.getOrdersByStatus(statusFilter.trim().toUpperCase());
        } else {
            orders = orderService.getAllOrders();
        }

        req.setAttribute("orders", orders);
        req.setAttribute("selectedStatus", statusFilter);
        req.getRequestDispatcher("/admin/orders.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));

            if ("updateStatus".equals(action)) {
                String newStatus = req.getParameter("orderStatus");
                orderService.updateOrderStatus(orderId, newStatus);
                session.setAttribute("flashSuccess", "Order #" + orderId + " status updated to " + newStatus + ".");
            } else if ("updatePayment".equals(action)) {
                String newPaymentStatus = req.getParameter("paymentStatus");
                orderService.updatePaymentStatus(orderId, newPaymentStatus);
                session.setAttribute("flashSuccess", "Order #" + orderId + " payment status updated to " + newPaymentStatus + ".");
            }
        } catch (ShopSphereException e) {
            session.setAttribute("flashError", e.getMessage());
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Invalid order reference.");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/orders");
    }
}
