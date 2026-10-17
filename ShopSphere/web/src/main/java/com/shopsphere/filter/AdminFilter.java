package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter 4 in Filter Chain:
 * Intercepts administrative routes (/admin/*) and enforces role-based access control (RBAC).
 * Anonymous users are redirected to login, while non-ADMIN authenticated users receive HTTP 403 Forbidden.
 */
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getRequestURI());
            return;
        }

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            req.setAttribute("errorMessage", "Access Denied: Administrative privileges required.");
            req.getRequestDispatcher("/error403.jsp").forward(req, res);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
