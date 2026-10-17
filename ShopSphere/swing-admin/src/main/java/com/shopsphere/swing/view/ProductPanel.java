package com.shopsphere.swing.view;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.swing.controller.ProductController;
import com.shopsphere.swing.model.ProductTableModel;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Product Panel View.
 * Implements JTable with custom AbstractTableModel, form controls (JTextField, JTextArea,
 * JComboBox, JRadioButton, ButtonGroup), and action buttons.
 * Pure view containing ZERO database code.
 */
public class ProductPanel extends JPanel {

    private final ProductController productController;
    private final ProductTableModel tableModel;

    private JTable productTable;
    private JTextField idField;
    private JTextField nameField;
    private JTextField brandField;
    private JComboBox<CategoryWrapper> categoryComboBox;
    private JTextField priceField;
    private JTextField discountField;
    private JTextField stockField;
    private JTextArea descArea;
    private JRadioButton activeRadio;
    private JRadioButton inactiveRadio;
    private ButtonGroup statusGroup;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton refreshButton;
    private JTextField searchField;

    private List<Product> allProducts = new ArrayList<>();

    public ProductPanel() {
        this.productController = new ProductController();
        this.tableModel = new ProductTableModel();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Top Search & Filter Bar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        topBar.add(new JLabel("Search Catalog:"));
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Filter");
        JButton resetSearchBtn = new JButton("Reset");
        refreshButton = new JButton("Reload from Database");

        searchBtn.addActionListener(e -> filterTable(searchField.getText()));
        resetSearchBtn.addActionListener(e -> {
            searchField.setText("");
            tableModel.setProducts(allProducts);
        });
        refreshButton.addActionListener(e -> loadData());

        topBar.add(searchField);
        topBar.add(searchBtn);
        topBar.add(resetSearchBtn);
        topBar.add(refreshButton);
        add(topBar, BorderLayout.NORTH);

        // 2. Center SplitPane: Left Table, Right Form
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowHeight(24);
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateForm(tableModel.getProductAt(selectedRow));
                }
            }
        });

        JScrollPane tableScroll = new JScrollPane(productTable);
        tableScroll.setPreferredSize(new Dimension(560, 400));

        // Form Panel
        JPanel formPanel = buildFormPanel();
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setPreferredSize(new Dimension(360, 400));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScroll, formScroll);
        splitPane.setDividerLocation(580);
        splitPane.setResizeWeight(0.65);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel buildFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Product Details / Editor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 6, 4, 6);

        // ID (Read-only)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        form.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        idField = new JTextField(8);
        idField.setEditable(false);
        idField.setBackground(new Color(241, 245, 249));
        form.add(idField, gbc);

        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        nameField = new JTextField();
        form.add(nameField, gbc);

        // Brand
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Brand:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        brandField = new JTextField();
        form.add(brandField, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        categoryComboBox = new JComboBox<>();
        form.add(categoryComboBox, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 4;
        form.add(new JLabel("Price (₹):"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        priceField = new JTextField();
        form.add(priceField, gbc);

        // Discount %
        gbc.gridx = 0; gbc.gridy = 5;
        form.add(new JLabel("Discount %:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        discountField = new JTextField();
        form.add(discountField, gbc);

        // Stock
        gbc.gridx = 0; gbc.gridy = 6;
        form.add(new JLabel("Stock Units:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        stockField = new JTextField();
        form.add(stockField, gbc);

        // Status (JRadioButton + ButtonGroup)
        gbc.gridx = 0; gbc.gridy = 7;
        form.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = 7;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        activeRadio = new JRadioButton("ACTIVE", true);
        inactiveRadio = new JRadioButton("INACTIVE");
        statusGroup = new ButtonGroup();
        statusGroup.add(activeRadio);
        statusGroup.add(inactiveRadio);
        radioPanel.add(activeRadio);
        radioPanel.add(inactiveRadio);
        form.add(radioPanel, gbc);

        // Description (JTextArea)
        gbc.gridx = 0; gbc.gridy = 8;
        form.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridy = 8;
        descArea = new JTextArea(4, 18);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        form.add(new JScrollPane(descArea), gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
        addButton = new JButton("Add New");
        updateButton = new JButton("Save Edit");
        deleteButton = new JButton("Deactivate");
        clearButton = new JButton("Clear Form");

        addButton.addActionListener(e -> onSaveProduct(true));
        updateButton.addActionListener(e -> onSaveProduct(false));
        deleteButton.addActionListener(e -> onDeleteProduct());
        clearButton.addActionListener(e -> clearForm());

        btnPanel.add(addButton);
        btnPanel.add(updateButton);
        btnPanel.add(deleteButton);
        btnPanel.add(clearButton);
        form.add(btnPanel, gbc);

        return form;
    }

    private void loadData() {
        productController.loadCategories(
                cats -> {
                    categoryComboBox.removeAllItems();
                    for (Category c : cats) {
                        categoryComboBox.addItem(new CategoryWrapper(c));
                    }
                },
                err -> JOptionPane.showMessageDialog(this, err, "Categories Error", JOptionPane.ERROR_MESSAGE)
        );

        productController.loadProducts(
                prods -> {
                    allProducts = prods;
                    tableModel.setProducts(prods);
                },
                err -> JOptionPane.showMessageDialog(this, err, "Products Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    private void populateForm(Product p) {
        if (p == null) return;
        idField.setText(String.valueOf(p.getId()));
        nameField.setText(p.getName());
        brandField.setText(p.getBrand());
        priceField.setText(p.getPrice() != null ? p.getPrice().toString() : "0.00");
        discountField.setText(p.getDiscountPercentage() != null ? p.getDiscountPercentage().toString() : "0.00");
        stockField.setText(String.valueOf(p.getStock()));
        descArea.setText(p.getDescription());

        if ("INACTIVE".equalsIgnoreCase(p.getStatus())) {
            inactiveRadio.setSelected(true);
        } else {
            activeRadio.setSelected(true);
        }

        // Select category in combo box
        for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
            CategoryWrapper cw = categoryComboBox.getItemAt(i);
            if (cw != null && cw.category.getId() == p.getCategoryId()) {
                categoryComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    private void clearForm() {
        productTable.clearSelection();
        idField.setText("");
        nameField.setText("");
        brandField.setText("");
        priceField.setText("");
        discountField.setText("0.00");
        stockField.setText("10");
        descArea.setText("");
        activeRadio.setSelected(true);
        if (categoryComboBox.getItemCount() > 0) {
            categoryComboBox.setSelectedIndex(0);
        }
    }

    private void onSaveProduct(boolean isNew) {
        String name = nameField.getText().trim();
        String brand = brandField.getText().trim();
        String priceStr = priceField.getText().trim();
        String discountStr = discountField.getText().trim();
        String stockStr = stockField.getText().trim();
        String desc = descArea.getText().trim();
        String status = activeRadio.isSelected() ? "ACTIVE" : "INACTIVE";

        CategoryWrapper cw = (CategoryWrapper) categoryComboBox.getSelectedItem();
        if (name.isEmpty() || brand.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || cw == null) {
            JOptionPane.showMessageDialog(this, "Please fill all mandatory fields (Name, Brand, Price, Stock, Category).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal price;
        BigDecimal discount;
        int stock;
        try {
            price = new BigDecimal(priceStr);
            discount = discountStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(discountStr);
            stock = Integer.parseInt(stockStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric input for Price, Discount, or Stock.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product p = new Product();
        if (!isNew) {
            String idStr = idField.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an existing product from the table to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            p.setId(Integer.parseInt(idStr));
        }
        p.setName(name);
        p.setBrand(brand);
        p.setCategoryId(cw.category.getId());
        p.setCategoryName(cw.category.getName());
        p.setPrice(price);
        p.setDiscountPercentage(discount);
        p.setStock(stock);
        p.setDescription(desc);
        p.setStatus(status);
        p.setImageUrl("images/products/placeholder.jpg");

        productController.saveProduct(
                p,
                isNew,
                () -> {
                    JOptionPane.showMessageDialog(this, "Product " + (isNew ? "created" : "updated") + " successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                },
                err -> JOptionPane.showMessageDialog(this, err, "Save Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    private void onDeleteProduct() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a product from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int pid = Integer.parseInt(idStr);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete / deactivate product #" + pid + "?", "Confirm Deactivation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productController.deleteProduct(
                    pid,
                    () -> {
                        JOptionPane.showMessageDialog(this, "Product deactivated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        loadData();
                    },
                    err -> JOptionPane.showMessageDialog(this, err, "Delete Error", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void filterTable(String query) {
        if (query == null || query.trim().isEmpty()) {
            tableModel.setProducts(allProducts);
            return;
        }
        String lower = query.trim().toLowerCase();
        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            if ((p.getName() != null && p.getName().toLowerCase().contains(lower)) ||
                (p.getBrand() != null && p.getBrand().toLowerCase().contains(lower)) ||
                (p.getCategoryName() != null && p.getCategoryName().toLowerCase().contains(lower))) {
                filtered.add(p);
            }
        }
        tableModel.setProducts(filtered);
    }

    /**
     * Helper wrapper for JComboBox display.
     */
    private static class CategoryWrapper {
        final Category category;

        CategoryWrapper(Category category) {
            this.category = category;
        }

        @Override
        public String toString() {
            return category != null ? category.getName() : "";
        }
    }
}
