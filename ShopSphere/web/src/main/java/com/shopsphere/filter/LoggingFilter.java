package com.shopsphere.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filter 2 in Filter Chain:
 * Intercepts every HTTP request, records start time, invokes downstream components,
 * and logs elapsed execution time, HTTP method, URI, and response status code.
 */
public class LoggingFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("[LoggingFilter] Initialized in filter chain");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();
        String uri = req.getRequestURI();
        String method = req.getMethod();

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = res.getStatus();
            LOGGER.info(String.format("[HTTP AUDIT] %s %s -> Status: %d | Time: %d ms", method, uri, status, duration));
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("[LoggingFilter] Destroyed");
    }
}
