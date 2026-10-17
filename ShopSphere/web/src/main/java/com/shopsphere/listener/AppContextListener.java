package com.shopsphere.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.Date;
import java.util.logging.Logger;

/**
 * ServletContextListener:
 * Intercepts application lifecycle events: container initialization and graceful shutdown.
 * Injects shared global attributes into ServletContext accessible via ${applicationScope.*}.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        LOGGER.info("=================================================");
        LOGGER.info("[ShopSphere] Web Application Context Initializing");
        LOGGER.info("Container: " + ctx.getServerInfo());
        LOGGER.info("Context Path: " + ctx.getContextPath());
        LOGGER.info("=================================================");

        // Inject application-scoped global attributes if not already present from web.xml
        if (ctx.getAttribute("appName") == null) {
            ctx.setAttribute("appName", "ShopSphere");
        }
        if (ctx.getAttribute("appVersion") == null) {
            ctx.setAttribute("appVersion", "1.0.0-RTU");
        }
        ctx.setAttribute("appStartupTime", new Date());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("=================================================");
        LOGGER.info("[ShopSphere] Web Application Context Shutting Down");
        LOGGER.info("=================================================");
    }
}
