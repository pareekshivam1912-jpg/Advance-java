package com.shopsphere.controller;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.ServiceFactory;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller handling user authentication and showcasing the complete Servlet Lifecycle.
 * Demonstrates init(ServletConfig), doGet, doPost, and destroy().
 */
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService userService;
    private CartService cartService;
    private String loginNotice;
    private int maxLoginAttempts;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Syllabus Demo: Reading ServletConfig and init-param configured in web.xml
        this.userService = ServiceFactory.getUserService();
        this.cartService = ServiceFactory.getCartService();
        this.loginNotice = config.getInitParameter("loginNotice");
        String maxAttemptsParam = config.getInitParameter("maxLoginAttempts");
        this.maxLoginAttempts = maxAttemptsParam != null ? Integer.parseInt(maxAttemptsParam) : 5;

        System.out.println("[Servlet Lifecycle] LoginServlet initialized. ServletName=" +
                config.getServletName() + ", loginNotice=" + loginNotice +
                ", maxLoginAttempts=" + maxLoginAttempts);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // If already logged in, redirect to home or admin
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/");
            }
            return;
        }

        // Pass init-param to request for display (Syllabus concept demo)
        req.setAttribute("loginNotice", this.loginNotice);
        req.setAttribute("redirectUrl", req.getParameter("redirect"));
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String redirect = req.getParameter("redirect");

        try {
            User user = userService.authenticate(email, password);

            // Session fixation protection: invalidate old session and create a brand new one
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = req.getSession(true);

            // Store user details in session
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());

            // Load initial cart count
            int cartCount = cartService.getCartCount(user.getId());
            session.setAttribute("cartCount", cartCount);

            session.setAttribute("flashMessage", "Welcome back, " + user.getName() + "!");
            session.setAttribute("flashType", "success");

            // Redirect based on role or intended destination
            if (user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("login")) {
                resp.sendRedirect(redirect.startsWith("/") ? req.getContextPath() + redirect : redirect);
            } else {
                resp.sendRedirect(req.getContextPath() + "/");
            }

        } catch (ShopSphereException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("email", email);
            req.setAttribute("redirectUrl", redirect);
            req.setAttribute("loginNotice", this.loginNotice);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        System.out.println("[Servlet Lifecycle] LoginServlet destroy() called. Releasing resources.");
        super.destroy();
    }
}
