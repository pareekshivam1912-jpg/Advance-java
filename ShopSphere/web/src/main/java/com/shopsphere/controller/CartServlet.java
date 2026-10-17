package com.shopsphere.controller;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller managing customer shopping cart operations:
 * view, add, update quantity, remove, and clear.
 */
@WebServlet(urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        this.cartService = ServiceFactory.getCartService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/cart");
            return;
        }

        List<CartItem> cartItems = cartService.getCart(user.getId());
        BigDecimal subtotal = cartService.calculateSubtotal(cartItems);

        int count = cartService.getCartCount(user.getId());
        session.setAttribute("cartCount", count);

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("subtotal", subtotal);
        req.getRequestDispatcher("/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String action = req.getParameter("action");
        if (action == null) {
            action = "view";
        }

        String referer = req.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = req.getContextPath() + "/cart";
        }

        if (user == null) {
            // Unauthenticated user trying to add/update cart
            String redirectTarget = referer;
            if (session == null) {
                session = req.getSession(true);
            }
            session.setAttribute("loginNotice", "Please sign in to add and manage items in your cart.");
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + redirectTarget);
            return;
        }

        try {
            switch (action) {
                case "add": {
                    int productId = Integer.parseInt(req.getParameter("productId"));
                    int quantity = 1;
                    String qtyParam = req.getParameter("quantity");
                    if (qtyParam != null && !qtyParam.trim().isEmpty()) {
                        quantity = Integer.parseInt(qtyParam.trim());
                    }

                    cartService.addToCart(user.getId(), productId, quantity);
                    session.setAttribute("flashSuccess", "Item successfully added to your cart.");
                    session.setAttribute("cartCount", cartService.getCartCount(user.getId()));
                    resp.sendRedirect(req.getContextPath() + "/cart");
                    break;
                }
                case "update": {
                    int productId = Integer.parseInt(req.getParameter("productId"));
                    int quantity = Integer.parseInt(req.getParameter("quantity"));

                    cartService.updateQuantity(user.getId(), productId, quantity);
                    session.setAttribute("flashSuccess", "Cart updated successfully.");
                    session.setAttribute("cartCount", cartService.getCartCount(user.getId()));
                    resp.sendRedirect(req.getContextPath() + "/cart");
                    break;
                }
                case "remove": {
                    int productId = Integer.parseInt(req.getParameter("productId"));
                    cartService.removeFromCart(user.getId(), productId);
                    session.setAttribute("flashSuccess", "Item removed from cart.");
                    session.setAttribute("cartCount", cartService.getCartCount(user.getId()));
                    resp.sendRedirect(req.getContextPath() + "/cart");
                    break;
                }
                case "clear": {
                    cartService.clearCart(user.getId());
                    session.setAttribute("flashSuccess", "Your shopping cart has been cleared.");
                    session.setAttribute("cartCount", 0);
                    resp.sendRedirect(req.getContextPath() + "/cart");
                    break;
                }
                default:
                    resp.sendRedirect(req.getContextPath() + "/cart");
                    break;
            }
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Invalid product ID or quantity format.");
            resp.sendRedirect(req.getContextPath() + "/cart");
        } catch (ShopSphereException e) {
            session.setAttribute("flashError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }
}
