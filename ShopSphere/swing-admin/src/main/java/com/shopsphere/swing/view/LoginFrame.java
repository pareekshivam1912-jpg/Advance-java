package com.shopsphere.swing.view;

import com.shopsphere.model.User;
import com.shopsphere.swing.controller.LoginController;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Login View.
 * Contains purely UI code (no database or business logic).
 * Delegates authentication commands to LoginController.
 */
public class LoginFrame extends JFrame {

    private final LoginController loginController;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private JButton loginButton;
    private JButton demoFillButton;
    private JLabel statusLabel;

    public LoginFrame() {
        this.loginController = new LoginController();
        initUI();
    }

    private void initUI() {
        setTitle("ShopSphere — Enterprise Desktop Admin Login");
        setSize(460, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(248, 250, 252));

        // 1. Header
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("ShopSphere Console", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitleLabel = new JLabel("Advanced Java Desktop Administration (MVC)", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(100, 116, 139));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2. Form (GridBagLayout)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        // Email
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel emailLbl = new JLabel("Admin Email:");
        emailLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(emailLbl, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        emailField = new JTextField(18);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(passLbl, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        passwordField = new JPasswordField(18);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(passwordField, gbc);

        // Show Password CheckBox
        gbc.gridx = 1; gbc.gridy = 2;
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheckBox.setOpaque(false);
        char defaultEcho = passwordField.getEchoChar();
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar(defaultEcho);
            }
        });
        formPanel.add(showPasswordCheckBox, gbc);

        // Status Feedback Label
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(220, 38, 38));
        formPanel.add(statusLabel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // 3. Buttons (Login & Demo Autofill)
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Login to Admin Console");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setBackground(new Color(37, 99, 235));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> performLogin());

        // Enter key shortcut
        getRootPane().setDefaultButton(loginButton);

        demoFillButton = new JButton("Use Seed Admin (admin@shopsphere.com)");
        demoFillButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        demoFillButton.setBackground(new Color(241, 245, 249));
        demoFillButton.setFocusPainted(false);
        demoFillButton.addActionListener(e -> {
            emailField.setText("admin@shopsphere.com");
            passwordField.setText("Admin@123");
            statusLabel.setText("Filled seed admin credentials.");
            statusLabel.setForeground(new Color(22, 101, 52));
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(demoFillButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void performLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        loginButton.setEnabled(false);
        statusLabel.setText("Authenticating with MySQL server...");
        statusLabel.setForeground(new Color(37, 99, 235));

        loginController.authenticate(
                email,
                password,
                user -> onLoginSuccess(user),
                error -> onLoginError(error)
        );
    }

    private void onLoginSuccess(User adminUser) {
        statusLabel.setText("Authentication successful! Loading dashboard...");
        statusLabel.setForeground(new Color(22, 101, 52));

        dispose(); // Close Login Frame
        AdminDashboard dashboard = new AdminDashboard(adminUser);
        dashboard.setVisible(true);
    }

    private void onLoginError(String message) {
        loginButton.setEnabled(true);
        statusLabel.setText(message);
        statusLabel.setForeground(new Color(220, 38, 38));
        JOptionPane.showMessageDialog(this, message, "Login Failed", JOptionPane.ERROR_MESSAGE);
    }
}
