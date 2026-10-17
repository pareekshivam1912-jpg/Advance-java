package com.shopsphere.controller;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.User;
import com.shopsphere.model.WishlistItem;
import com.shopsphere.service.CartService;
import com.shopsphere.service.ServiceFactory;
import com.shopsphere.service.WishlistService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller managing customer saved wishlist items and transitioning items into the shopping cart.
 */
@WebServlet(urlPatterns = {"/wishlist"})
public class WishlistServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private WishlistService wishlistService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        this.wishlistService = ServiceFactory.getWishlistService();
        this.cartService = ServiceFactory.getCartService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/wishlist");
            return;
        }

        List<WishlistItem> wishlistItems = wishlistService.getWishlist(user.getId());
        req.setAttribute("wishlistItems", wishlistItems);
        req.getRequestDispatcher("/wishlist.jsp").forward(req, resp);
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
            referer = req.getContextPath() + "/wishlist";
        }

        if (user == null) {
            if (session == null) {
                session = req.getSession(true);
            }
            session.setAttribute("loginNotice", "Please sign in to save and manage items in your wishlist.");
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + referer);
            return;
        }

        try {
            int productId = Integer.parseInt(req.getParameter("productId"));

            switch (action) {
                case "add": {
                    wishlistService.addToWishlist(user.getId(), productId);
                    session.setAttribute("flashSuccess", "Product saved to your wishlist.");
                    resp.sendRedirect(referer);
                    break;
                }
                case "remove": {
                    wishlistService.removeFromWishlist(user.getId(), productId);
                    session.setAttribute("flashSuccess", "Item removed from your wishlist.");
                    resp.sendRedirect(req.getContextPath() + "/wishlist");
                    break;
                }
                case "moveToCart": {
                    wishlistService.moveToCart(user.getId(), productId);
                    session.setAttribute("cartCount", cartService.getCartCount(user.getId()));
                    session.setAttribute("flashSuccess", "Item moved from wishlist into your shopping cart.");
                    resp.sendRedirect(req.getContextPath() + "/cart");
                    break;
                }
                default:
                    resp.sendRedirect(req.getContextPath() + "/wishlist");
                    break;
            }
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Invalid product reference.");
            resp.sendRedirect(req.getContextPath() + "/wishlist");
        } catch (ShopSphereException e) {
            session.setAttribute("flashError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/wishlist");
        }
    }
}
