package com.shopsphere.controller;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Address;
import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.AddressService;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CheckoutService;
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
 * Controller managing checkout flow:
 * address selection, address creation, coupon discount calculation,
 * mock payment simulation, and server-side atomic order placement.
 */
@WebServlet(urlPatterns = {"/checkout", "/order-success"})
public class CheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CheckoutService checkoutService;
    private AddressService addressService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        this.checkoutService = ServiceFactory.getCheckoutService();
        this.addressService = ServiceFactory.getAddressService();
        this.cartService = ServiceFactory.getCartService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/checkout");
            return;
        }

        String path = req.getServletPath();
        if ("/order-success".equals(path)) {
            String orderIdStr = req.getParameter("orderId");
            req.setAttribute("orderId", orderIdStr);
            req.getRequestDispatcher("/order-success.jsp").forward(req, resp);
            return;
        }

        String couponCode = req.getParameter("coupon");
        try {
            CheckoutService.OrderSummary summary = checkoutService.computeSummary(user.getId(), couponCode);
            List<Address> addresses = addressService.getUserAddresses(user.getId());

            req.setAttribute("summary", summary);
            req.setAttribute("addresses", addresses);
            req.setAttribute("couponCode", couponCode);
            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
        } catch (ShopSphereException e) {
            session.setAttribute("flashError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/checkout");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "placeOrder";
        }

        try {
            if ("addAddress".equals(action)) {
                String fullName = req.getParameter("fullName");
                String phone = req.getParameter("phone");
                String street = req.getParameter("streetAddress");
                String city = req.getParameter("city");
                String state = req.getParameter("state");
                String postalCode = req.getParameter("postalCode");
                boolean isDefault = "true".equalsIgnoreCase(req.getParameter("isDefault")) || req.getParameter("isDefault") != null;

                addressService.addAddress(user.getId(), fullName, phone, street, city, state, postalCode, isDefault);
                session.setAttribute("flashSuccess", "Delivery address added successfully.");
                String coupon = req.getParameter("coupon");
                resp.sendRedirect(req.getContextPath() + "/checkout" + (coupon != null && !coupon.isEmpty() ? "?coupon=" + coupon : ""));
                return;
            }

            if ("applyCoupon".equals(action)) {
                String couponCode = req.getParameter("couponCode");
                resp.sendRedirect(req.getContextPath() + "/checkout?coupon=" + (couponCode != null ? couponCode.trim() : ""));
                return;
            }

            // Default action: placeOrder
            String addressIdParam = req.getParameter("addressId");
            if (addressIdParam == null || addressIdParam.trim().isEmpty()) {
                session.setAttribute("flashError", "Please select or add a delivery address.");
                resp.sendRedirect(req.getContextPath() + "/checkout");
                return;
            }

            int addressId = Integer.parseInt(addressIdParam.trim());
            String couponCode = req.getParameter("couponCode");
            String paymentMethod = req.getParameter("paymentMethod");
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                paymentMethod = "COD";
            }

            // CRITICAL SECURITY PROPERTY:
            // Notice that incoming form parameters for total amount or discount are completely ignored.
            // placeOrder calls computeSummary internally to recalculate everything directly from MySQL.
            Order order = checkoutService.placeOrder(user.getId(), addressId, couponCode, paymentMethod.trim().toUpperCase());

            // Clear session cart count
            session.setAttribute("cartCount", 0);
            session.setAttribute("flashSuccess", "Your order #" + order.getId() + " has been placed successfully!");

            resp.sendRedirect(req.getContextPath() + "/order-success?orderId=" + order.getId());

        } catch (ShopSphereException e) {
            session.setAttribute("flashError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/checkout");
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Invalid form data format.");
            resp.sendRedirect(req.getContextPath() + "/checkout");
        }
    }
}
