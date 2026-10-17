package com.shopsphere.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * HttpSessionListener & HttpSessionAttributeListener:
 * Tracks active HTTP sessions across the server, increments/decrements concurrent
 * active sessions counter, and exposes it to ServletContext for display in the Admin Dashboard.
 */
@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    private static final Logger LOGGER = Logger.getLogger(SessionListener.class.getName());
    private static final AtomicInteger ACTIVE_SESSIONS = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.incrementAndGet();
        ServletContext ctx = se.getSession().getServletContext();
        ctx.setAttribute("activeSessionCount", count);
        LOGGER.info(String.format("[SessionListener] New Session Created: %s | Active Count: %d", se.getSession().getId(), count));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.decrementAndGet();
        if (count < 0) {
            count = 0;
            ACTIVE_SESSIONS.set(0);
        }
        ServletContext ctx = se.getSession().getServletContext();
        ctx.setAttribute("activeSessionCount", count);
        LOGGER.info(String.format("[SessionListener] Session Destroyed: %s | Active Count: %d", se.getSession().getId(), count));
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        LOGGER.fine(String.format("[SessionAttribute] Added: %s=%s", event.getName(), event.getValue()));
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        LOGGER.fine(String.format("[SessionAttribute] Removed: %s", event.getName()));
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        LOGGER.fine(String.format("[SessionAttribute] Replaced: %s", event.getName()));
    }

    public static int getActiveSessions() {
        return ACTIVE_SESSIONS.get();
    }
}
