package com.shopsphere.controller;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.User;
import com.shopsphere.service.ReviewService;
import com.shopsphere.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller accepting customer product reviews and updating aggregated ratings.
 */
@WebServlet(urlPatterns = {"/review"})
public class ReviewServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        this.reviewService = ServiceFactory.getReviewService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String productIdParam = req.getParameter("productId");
        if (productIdParam == null || productIdParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/product?id=" + productIdParam);
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam.trim());
            int rating = Integer.parseInt(req.getParameter("rating"));
            String comment = req.getParameter("comment");

            reviewService.addReview(user.getId(), productId, rating, comment);
            session.setAttribute("flashSuccess", "Thank you! Your review and rating have been posted.");
            resp.sendRedirect(req.getContextPath() + "/product?id=" + productId);

        } catch (ShopSphereException e) {
            session.setAttribute("flashError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/product?id=" + productIdParam);
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Invalid review parameters submitted.");
            resp.sendRedirect(req.getContextPath() + "/product?id=" + productIdParam);
        }
    }
}
