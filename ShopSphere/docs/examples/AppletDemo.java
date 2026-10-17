package com.shopsphere.docs.examples;

/*
 * =========================================================================================
 * RTU ADVANCED JAVA SYLLABUS: HISTORICAL / CONCEPTUAL DEMO ONLY
 *
 * NOTE: This source file is located in docs/examples/ and is intentionally NOT part of the
 * Maven build reactor.
 *
 * Applets were deprecated in Java 9 (JEP 289), marked for removal in Java 17 (JEP 398),
 * and completely removed in modern JDK releases. In accordance with the RTU syllabus
 * guidelines, this code illustrates the classical Applet lifecycle and execution model
 * for university examination and viva evaluation.
 * =========================================================================================
 */

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Historical Java Applet demonstrating the 5 Lifecycle Methods:
 * 1. init()    - Called once by the browser/appletviewer when the applet is loaded.
 * 2. start()   - Called when the browser enters or re-enters the web page.
 * 3. paint()   - Called whenever the applet window requires repainting.
 * 4. stop()    - Called when the user navigates away from the web page.
 * 5. destroy() - Called when the browser shuts down to release resources.
 *
 * HTML Embedding Tag (Historically used):
 * <applet code="com.shopsphere.docs.examples.AppletDemo.class" width="400" height="250">
 *     <param name="siteName" value="ShopSphere E-Commerce"/>
 * </applet>
 */
public class AppletDemo extends Applet {

    private String siteName = "ShopSphere";
    private int hitCounter = 0;
    private String lifecycleStage = "UNINITIALIZED";

    /**
     * 1. init(): First method called. Initializes applet parameters and GUI state.
     */
    @Override
    public void init() {
        String param = getParameter("siteName");
        if (param != null) {
            this.siteName = param;
        }
        setBackground(new Color(240, 244, 248));
        this.lifecycleStage = "INITIALIZED via init()";
        System.out.println("[Applet Lifecycle] 1. init() invoked.");
    }

    /**
     * 2. start(): Invoked after init(), and every time the user revisits the page.
     */
    @Override
    public void start() {
        hitCounter++;
        this.lifecycleStage = "RUNNING via start() (Visits: " + hitCounter + ")";
        System.out.println("[Applet Lifecycle] 2. start() invoked.");
        repaint();
    }

    /**
     * 3. paint(Graphics g): Called to render visuals on the applet canvas.
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(30, 41, 59));
        g.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g.drawString("RTU Advanced Java: Java Applet Architecture", 20, 35);

        g.setColor(new Color(37, 99, 235));
        g.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g.drawString("Application: " + siteName, 20, 70);
        g.drawString("Current State: " + lifecycleStage, 20, 95);

        g.setColor(new Color(220, 38, 38));
        g.drawString("Status: DEPRECATED / OBSOLETE IN MODERN JAVA", 20, 130);

        g.setColor(new Color(71, 85, 105));
        g.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        g.drawString("Replaced in ShopSphere by: Java Swing Desktop + HTML5/Bootstrap Web UI", 20, 165);
    }

    /**
     * 4. stop(): Suspends background animations/timers when browser leaves page.
     */
    @Override
    public void stop() {
        this.lifecycleStage = "SUSPENDED via stop()";
        System.out.println("[Applet Lifecycle] 4. stop() invoked.");
    }

    /**
     * 5. destroy(): Cleans up allocated threads, memory, or native resources.
     */
    @Override
    public void destroy() {
        this.lifecycleStage = "DESTROYED via destroy()";
        System.out.println("[Applet Lifecycle] 5. destroy() invoked.");
    }
}
