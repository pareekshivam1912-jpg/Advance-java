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
 * Controller managing customer order history, order status timeline tracking, and order cancellation.
 */
@WebServlet(urlPatterns = {"/orders", "/order"})
public class OrderServlet extends HttpServlet {

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

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getRequestURI());
            return;
        }

        String path = req.getServletPath();
        if ("/order".equals(path)) {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/orders");
                return;
            }

            try {
                int orderId = Integer.parseInt(idParam.trim());
                Order order = orderService.getOrderDetails(orderId);
                if (order == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                    return;
                }

                // Check authorization
                if (order.getUserId() != user.getId() && !"ADMIN".equalsIgnoreCase(user.getRole())) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied to requested order.");
                    return;
                }

                req.setAttribute("order", order);
                req.getRequestDispatcher("/order-details.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/orders");
            }
        } else {
            List<Order> orders = orderService.getOrdersByUser(user.getId());
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/orders.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if ("cancel".equals(action)) {
            String idParam = req.getParameter("orderId");
            try {
                int orderId = Integer.parseInt(idParam.trim());
                orderService.cancelOrder(orderId, user.getId());
                session.setAttribute("flashSuccess", "Order #" + orderId + " has been successfully cancelled. Restored product stock.");
                resp.sendRedirect(req.getContextPath() + "/order?id=" + orderId);
            } catch (ShopSphereException e) {
                session.setAttribute("flashError", e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/order?id=" + idParam);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/orders");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/orders");
        }
    }
}
