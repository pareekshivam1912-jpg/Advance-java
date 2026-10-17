package com.shopsphere.swing.view;

import com.shopsphere.model.User;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Main Administrative Dashboard.
 * Features:
 * 1. JMenuBar with file operations, Help dialog, and runtime Pluggable Look and Feel switcher.
 * 2. JTabbedPane organizing modular MVC views (Products, Categories, Customers, Orders, Inventory).
 * 3. Status bar displaying authenticated user credentials and active Look and Feel engine.
 */
public class AdminDashboard extends JFrame {

    private final User currentAdmin;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;

    public AdminDashboard(User adminUser) {
        this.currentAdmin = adminUser;
        initUI();
    }

    private void initUI() {
        setTitle("ShopSphere — Enterprise Desktop Management Console");
        setSize(1100, 720);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 1. Menu Bar with Pluggable Look and Feel
        setJMenuBar(createMenuBar());

        // 2. Tabbed Pane (Main View Container)
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tabbedPane.addTab("Product Catalog", new ProductPanel());
        tabbedPane.addTab("Category Hierarchy", new CategoryPanel());
        tabbedPane.addTab("Customer Accounts", new CustomerPanel());
        tabbedPane.addTab("Customer Orders", new OrderPanel());
        tabbedPane.addTab("Low-Stock Alerts", new InventoryPanel());

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // 3. Status Bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        statusBar.setBackground(new Color(241, 245, 249));

        statusLabel = new JLabel("Logged in as: " + currentAdmin.getFullName() + " (" + currentAdmin.getEmail() + ") | Role: " + currentAdmin.getRole() + " | Database: MySQL 9.7 (Port 3306)");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(71, 85, 105));

        JLabel lafLabel = new JLabel("L&F: " + UIManager.getLookAndFeel().getName());
        lafLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lafLabel.setForeground(new Color(71, 85, 105));

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(lafLabel, BorderLayout.EAST);
        getContentPane().add(statusBar, BorderLayout.SOUTH);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // --- File Menu ---
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> onLogout());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // --- Look and Feel Menu (Pluggable UI Demo) ---
        JMenu lafMenu = new JMenu("Look & Feel");
        lafMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JMenuItem systemLafItem = new JMenuItem("System Native");
        systemLafItem.addActionListener(e -> changeLookAndFeel(UIManager.getSystemLookAndFeelClassName()));

        JMenuItem nimbusLafItem = new JMenuItem("Nimbus");
        nimbusLafItem.addActionListener(e -> changeLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"));

        JMenuItem metalLafItem = new JMenuItem("Metal (Java Default)");
        metalLafItem.addActionListener(e -> changeLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()));

        lafMenu.add(systemLafItem);
        lafMenu.add(nimbusLafItem);
        lafMenu.add(metalLafItem);
        menuBar.add(lafMenu);

        // --- Help Menu ---
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JMenuItem aboutItem = new JMenuItem("About ShopSphere Admin");
        aboutItem.addActionListener(e -> showAboutDialog());

        JMenuItem vivaItem = new JMenuItem("RTU Viva Architecture Notes");
        vivaItem.addActionListener(e -> showArchitectureNotes());

        helpMenu.add(aboutItem);
        helpMenu.add(vivaItem);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void changeLookAndFeel(String lafClassName) {
        try {
            UIManager.setLookAndFeel(lafClassName);
            SwingUtilities.updateComponentTreeUI(this);
            JOptionPane.showMessageDialog(this, "Look & Feel switched to: " + UIManager.getLookAndFeel().getName(), "UI Updated", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not set Look & Feel: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Log out from administrative console?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        }
    }

    private void showAboutDialog() {
        String msg = "ShopSphere — Enterprise Java E-Commerce Desktop Administration\n"
                + "Architecture: Java Swing Model-View-Controller (MVC)\n"
                + "Concurrency: Worker threads via javax.swing.SwingWorker (EDT safety)\n"
                + "Shared Backend: Core DAO + Service layer + MySQL 9.7\n"
                + "RTU Advanced Java Capstone Project (6CS4-04)";
        JOptionPane.showMessageDialog(this, msg, "About ShopSphere Admin", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showArchitectureNotes() {
        String notes = "RTU Advanced Java Examination Highlights:\n\n"
                + "1. Model-View-Controller (MVC):\n"
                + "   - Model: Domain entities (Product, Category, Order) & custom AbstractTableModels.\n"
                + "   - View: LoginFrame, AdminDashboard, ProductPanel, etc. (Zero JDBC/SQL inside views).\n"
                + "   - Controller: ProductController, OrderController, etc. interfacing with ServiceFactory.\n\n"
                + "2. Event Dispatch Thread (EDT) Safety:\n"
                + "   - All database network queries execute in background threads using SwingWorker.\n"
                + "   - GUI updates trigger strictly inside done() on the EDT, preventing UI freezes.\n\n"
                + "3. Pluggable Look and Feel:\n"
                + "   - Dynamic runtime UI skin switching via UIManager and SwingUtilities.updateComponentTreeUI().";
        JOptionPane.showMessageDialog(this, notes, "RTU MVC Architecture Notes", JOptionPane.INFORMATION_MESSAGE);
    }
}
