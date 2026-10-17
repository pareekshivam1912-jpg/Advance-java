package com.shopsphere.swing;

import com.shopsphere.swing.view.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * RTU Advanced Java Syllabus Core Requirement: Java Swing MVC Desktop Application.
 *
 * Entrypoint:
 * 1. Configures Pluggable Look and Feel (System Native L&F).
 * 2. Launches LoginFrame on the Event Dispatch Thread (EDT) via SwingUtilities.invokeLater.
 */
public class SwingAdminApp {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("   SHOPSPHERE — RTU ADVANCED JAVA SYLLABUS: SWING DESKTOP ADMIN (MVC)     ");
        System.out.println("==========================================================================");
        System.out.println("Initializing Pluggable Look and Feel...");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system Look & Feel, defaulting to Metal: " + e.getMessage());
        }

        System.out.println("Launching LoginFrame on the Event Dispatch Thread (EDT)...");
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
