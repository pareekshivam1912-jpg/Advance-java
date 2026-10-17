package com.shopsphere.listener;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;

import java.util.logging.Logger;

/**
 * ServletRequestListener:
 * Intercepts individual request lifecycle events (request initialization and release).
 * Demonstrates request-scoped auditing for academic syllabus requirements.
 */
@WebListener
public class AppRequestListener implements ServletRequestListener {

    private static final Logger LOGGER = Logger.getLogger(AppRequestListener.class.getName());

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        if (sre.getServletRequest() instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) sre.getServletRequest();
            req.setAttribute("requestStartTimeNano", System.nanoTime());
        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        if (sre.getServletRequest() instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) sre.getServletRequest();
            Object startObj = req.getAttribute("requestStartTimeNano");
            if (startObj instanceof Long) {
                long elapsedNanos = System.nanoTime() - (Long) startObj;
                LOGGER.fine(String.format("[RequestAudit] Request %s released in %.2f ms", req.getRequestURI(), elapsedNanos / 1_000_000.0));
            }
        }
    }
}
