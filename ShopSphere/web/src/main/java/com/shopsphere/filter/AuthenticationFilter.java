package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Filter 3 in Filter Chain:
 * Intercepts customer protected routes (/cart, /checkout, /orders, /order, /profile, /wishlist, /review)
 * and guarantees that only authenticated sessions proceed. Unauthenticated requests are redirected
 * to the login page with an encoded return parameter.
 */
public class AuthenticationFilter implements Filter {

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
            String targetUri = req.getRequestURI();
            String query = req.getQueryString();
            if (query != null && !query.trim().isEmpty()) {
                targetUri += "?" + query;
            }
            String encodedTarget = URLEncoder.encode(targetUri, StandardCharsets.UTF_8);

            if (session == null) {
                session = req.getSession(true);
            }
            session.setAttribute("loginNotice", "Authentication required to access this resource.");
            res.sendRedirect(req.getContextPath() + "/login?redirect=" + encodedTarget);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
