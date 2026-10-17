package com.shopsphere.swing.view;

import com.shopsphere.model.Product;
import com.shopsphere.swing.controller.InventoryController;
import com.shopsphere.swing.model.InventoryTableModel;

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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Inventory & Low-Stock Alerts Panel.
 */
public class InventoryPanel extends JPanel {

    private final InventoryController inventoryController;
    private final InventoryTableModel tableModel;

    private JTable inventoryTable;
    private JComboBox<Integer> thresholdCombo;
    private JLabel summaryLabel;

    public InventoryPanel() {
        this.inventoryController = new InventoryController();
        this.tableModel = new InventoryTableModel();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Toolbar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        topBar.add(new JLabel("Stock Threshold Limit:"));

        thresholdCombo = new JComboBox<>(new Integer[]{5, 10, 15, 20, 50});
        thresholdCombo.setSelectedItem(15);
        thresholdCombo.addActionListener(e -> loadData());
        topBar.add(thresholdCombo);

        JButton reloadBtn = new JButton("Refresh Low-Stock Alerts");
        reloadBtn.addActionListener(e -> loadData());
        topBar.add(reloadBtn);

        summaryLabel = new JLabel(" ");
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        summaryLabel.setForeground(new Color(220, 38, 38));
        topBar.add(summaryLabel);

        add(topBar, BorderLayout.NORTH);

        // Center Table
        inventoryTable = new JTable(tableModel);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryTable.setRowHeight(24);
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        // Bottom Operations Bar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        bottomBar.add(new JLabel("Quick Warehouse Restock:"));

        JButton add10Btn = new JButton("+10 Units");
        JButton add25Btn = new JButton("+25 Units");
        JButton add50Btn = new JButton("+50 Units");

        add10Btn.addActionListener(e -> onQuickRestock(10));
        add25Btn.addActionListener(e -> onQuickRestock(25));
        add50Btn.addActionListener(e -> onQuickRestock(50));

        bottomBar.add(add10Btn);
        bottomBar.add(add25Btn);
        bottomBar.add(add50Btn);

        add(bottomBar, BorderLayout.SOUTH);
    }

    private void loadData() {
        int threshold = (Integer) thresholdCombo.getSelectedItem();
        inventoryController.loadLowStock(
                threshold,
                list -> {
                    tableModel.setProducts(list);
                    summaryLabel.setText("Found " + list.size() + " items below threshold (" + threshold + ")");
                },
                err -> JOptionPane.showMessageDialog(this, err, "Inventory Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    private void onQuickRestock(int unitsToAdd) {
        int row = inventoryTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a product from the alert table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product p = tableModel.getProductAt(row);
        if (p == null) return;

        inventoryController.restockProduct(
                p.getId(),
                p.getStock(),
                unitsToAdd,
                () -> {
                    JOptionPane.showMessageDialog(this, "Restocked " + p.getName() + " (+" + unitsToAdd + " units).", "Restock Successful", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                },
                err -> JOptionPane.showMessageDialog(this, err, "Restock Error", JOptionPane.ERROR_MESSAGE)
        );
    }
}
