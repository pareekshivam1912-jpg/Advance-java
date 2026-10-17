package com.shopsphere.swing.view;

import com.shopsphere.model.User;
import com.shopsphere.swing.controller.CustomerController;
import com.shopsphere.swing.model.CustomerTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Customer Accounts Panel.
 */
public class CustomerPanel extends JPanel {

    private final CustomerController customerController;
    private final CustomerTableModel tableModel;

    private JTable customerTable;
    private JTextField searchField;
    private JButton toggleStatusBtn;
    private List<User> allUsers = new ArrayList<>();

    public CustomerPanel() {
        this.customerController = new CustomerController();
        this.tableModel = new CustomerTableModel();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Toolbar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        topBar.add(new JLabel("Search Customers:"));
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Filter");
        JButton resetBtn = new JButton("Reset");
        JButton reloadBtn = new JButton("Reload");

        searchBtn.addActionListener(e -> filter(searchField.getText()));
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            tableModel.setUsers(allUsers);
        });
        reloadBtn.addActionListener(e -> loadData());

        topBar.add(searchField);
        topBar.add(searchBtn);
        topBar.add(resetBtn);
        topBar.add(reloadBtn);
        add(topBar, BorderLayout.NORTH);

        // Center Table
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setRowHeight(24);
        customerTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(new JScrollPane(customerTable), BorderLayout.CENTER);

        // Bottom Actions
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        toggleStatusBtn = new JButton("Toggle Account Status (Active/Inactive)");
        toggleStatusBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        toggleStatusBtn.addActionListener(e -> onToggleStatus());

        bottomBar.add(toggleStatusBtn);
        add(bottomBar, BorderLayout.SOUTH);
    }

    private void loadData() {
        customerController.loadCustomers(
                users -> {
                    allUsers = users;
                    tableModel.setUsers(users);
                },
                err -> JOptionPane.showMessageDialog(this, err, "Customer Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    private void filter(String query) {
        if (query == null || query.trim().isEmpty()) {
            tableModel.setUsers(allUsers);
            return;
        }
        String lower = query.trim().toLowerCase();
        List<User> list = new ArrayList<>();
        for (User u : allUsers) {
            if ((u.getFullName() != null && u.getFullName().toLowerCase().contains(lower)) ||
                (u.getEmail() != null && u.getEmail().toLowerCase().contains(lower)) ||
                (u.getRole() != null && u.getRole().toLowerCase().contains(lower))) {
                list.add(u);
            }
        }
        tableModel.setUsers(list);
    }

    private void onToggleStatus() {
        int row = customerTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a customer account from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User u = tableModel.getUserAt(row);
        if (u == null) return;

        if ("ADMIN".equalsIgnoreCase(u.getRole())) {
            JOptionPane.showMessageDialog(this, "Administrator accounts cannot be toggled from this panel.", "Security Protection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Change status for " + u.getEmail() + " from " + u.getStatus() + " to " + ("ACTIVE".equalsIgnoreCase(u.getStatus()) ? "INACTIVE" : "ACTIVE") + "?",
                "Confirm Status Change",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            customerController.toggleCustomerStatus(
                    u.getId(),
                    u.getStatus(),
                    () -> {
                        JOptionPane.showMessageDialog(this, "Account status updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                    },
                    err -> JOptionPane.showMessageDialog(this, err, "Update Error", JOptionPane.ERROR_MESSAGE)
            );
        }
    }
}
