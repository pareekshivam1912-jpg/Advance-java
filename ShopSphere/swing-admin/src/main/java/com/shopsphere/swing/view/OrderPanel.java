package com.shopsphere.swing.view;

import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.swing.controller.OrderController;
import com.shopsphere.swing.model.OrderTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Order Operations Panel.
 */
public class OrderPanel extends JPanel {

    private final OrderController orderController;
    private final OrderTableModel tableModel;

    private JTable orderTable;
    private JComboBox<String> statusFilterCombo;
    private JComboBox<String> updateStatusCombo;
    private List<Order> allOrders = new ArrayList<>();

    private static final String[] ORDER_STATUSES = {"PLACED", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"};

    public OrderPanel() {
        this.orderController = new OrderController();
        this.tableModel = new OrderTableModel();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Toolbar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        topBar.add(new JLabel("Filter by Status:"));

        statusFilterCombo = new JComboBox<>(new String[]{"ALL", "PLACED", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"});
        statusFilterCombo.addActionListener(e -> filterOrders((String) statusFilterCombo.getSelectedItem()));
        topBar.add(statusFilterCombo);

        JButton reloadBtn = new JButton("Reload Orders");
        reloadBtn.addActionListener(e -> loadData());
        topBar.add(reloadBtn);
        add(topBar, BorderLayout.NORTH);

        // Center Table
        orderTable = new JTable(tableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.setRowHeight(24);
        orderTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(new JScrollPane(orderTable), BorderLayout.CENTER);

        // Bottom Operations Bar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));

        JButton detailsBtn = new JButton("View Order Items");
        detailsBtn.addActionListener(e -> onViewDetails());
        bottomBar.add(detailsBtn);

        bottomBar.add(new JLabel("Set Status:"));
        updateStatusCombo = new JComboBox<>(ORDER_STATUSES);
        bottomBar.add(updateStatusCombo);

        JButton updateBtn = new JButton("Apply Status Update");
        updateBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        updateBtn.addActionListener(e -> onUpdateStatus());
        bottomBar.add(updateBtn);

        add(bottomBar, BorderLayout.SOUTH);
    }

    private void loadData() {
        orderController.loadOrders(
                orders -> {
                    allOrders = orders;
                    filterOrders((String) statusFilterCombo.getSelectedItem());
                },
                err -> JOptionPane.showMessageDialog(this, err, "Order Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    private void filterOrders(String status) {
        if (status == null || "ALL".equalsIgnoreCase(status)) {
            tableModel.setOrders(allOrders);
            return;
        }
        List<Order> filtered = new ArrayList<>();
        for (Order o : allOrders) {
            if (status.equalsIgnoreCase(o.getOrderStatus())) {
                filtered.add(o);
            }
        }
        tableModel.setOrders(filtered);
    }

    private void onUpdateStatus() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an order from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Order o = tableModel.getOrderAt(row);
        if (o == null) return;

        String targetStatus = (String) updateStatusCombo.getSelectedItem();
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Change Order #" + o.getId() + " status from " + o.getOrderStatus() + " to " + targetStatus + "?",
                "Confirm Order Status Transition",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            orderController.updateOrderStatus(
                    o.getId(),
                    targetStatus,
                    () -> {
                        JOptionPane.showMessageDialog(this, "Order #" + o.getId() + " updated to " + targetStatus, "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                    },
                    err -> JOptionPane.showMessageDialog(this, err, "Update Error", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void onViewDetails() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an order to view items.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Order o = tableModel.getOrderAt(row);
        if (o == null) return;

        orderController.getOrderDetails(
                o.getId(),
                orderWithItems -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Order Details #").append(orderWithItems.getId()).append("\n");
                    sb.append("Customer: ").append(orderWithItems.getUserFullName()).append("\n");
                    sb.append("Status: ").append(orderWithItems.getOrderStatus()).append("\n");
                    sb.append("Total: ₹").append(orderWithItems.getTotalAmount()).append("\n\n");
                    sb.append("Ordered Items:\n");

                    List<OrderItem> items = orderWithItems.getItems();
                    if (items != null && !items.isEmpty()) {
                        for (OrderItem it : items) {
                            sb.append("  • ").append(it.getProductName())
                              .append(" (Qty: ").append(it.getQuantity())
                              .append(", ₹").append(it.getPrice()).append(" each)\n");
                        }
                    } else {
                        sb.append("  (No item records attached)\n");
                    }

                    JOptionPane.showMessageDialog(this, sb.toString(), "Order #" + orderWithItems.getId() + " Line Items", JOptionPane.INFORMATION_MESSAGE);
                },
                err -> JOptionPane.showMessageDialog(this, err, "Details Error", JOptionPane.ERROR_MESSAGE)
        );
    }
}
