package com.shopsphere.swing.view;

import com.shopsphere.model.Category;
import com.shopsphere.swing.controller.CategoryController;
import com.shopsphere.swing.model.CategoryTableModel;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
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

/**
 * RTU Advanced Java Syllabus: Swing MVC - Category Panel View.
 */
public class CategoryPanel extends JPanel {

    private final CategoryController categoryController;
    private final CategoryTableModel tableModel;

    private JTable categoryTable;
    private JTextField idField;
    private JTextField nameField;
    private JTextArea descArea;
    private JRadioButton activeRadio;
    private JRadioButton inactiveRadio;
    private ButtonGroup statusGroup;

    public CategoryPanel() {
        this.categoryController = new CategoryController();
        this.tableModel = new CategoryTableModel();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Toolbar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Reload Categories");
        refreshBtn.addActionListener(e -> loadData());
        topBar.add(refreshBtn);
        add(topBar, BorderLayout.NORTH);

        // Center Table
        categoryTable = new JTable(tableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.setRowHeight(24);
        categoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        categoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = categoryTable.getSelectedRow();
                if (row >= 0) {
                    populateForm(tableModel.getCategoryAt(row));
                }
            }
        });

        JScrollPane tableScroll = new JScrollPane(categoryTable);
        tableScroll.setPreferredSize(new Dimension(500, 400));

        // Form Panel
        JPanel form = buildForm();
        JScrollPane formScroll = new JScrollPane(form);
        formScroll.setPreferredSize(new Dimension(360, 400));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScroll, formScroll);
        split.setDividerLocation(520);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Category Editor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        // ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        form.add(new JLabel("Category ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        idField = new JTextField(8);
        idField.setEditable(false);
        idField.setBackground(new Color(241, 245, 249));
        form.add(idField, gbc);

        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        nameField = new JTextField();
        form.add(nameField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        descArea = new JTextArea(4, 18);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        form.add(new JScrollPane(descArea), gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        activeRadio = new JRadioButton("ACTIVE", true);
        inactiveRadio = new JRadioButton("INACTIVE");
        statusGroup = new ButtonGroup();
        statusGroup.add(activeRadio);
        statusGroup.add(inactiveRadio);
        radioPanel.add(activeRadio);
        radioPanel.add(inactiveRadio);
        form.add(radioPanel, gbc);

        // Action Buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        JButton addBtn = new JButton("Create");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Deactivate");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> onSave(true));
        updateBtn.addActionListener(e -> onSave(false));
        deleteBtn.addActionListener(e -> onDelete());
        clearBtn.addActionListener(e -> clearForm());

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);
        form.add(btnPanel, gbc);

        return form;
    }

    private void loadData() {
        categoryController.loadCategories(
                cats -> tableModel.setCategories(cats),
                err -> JOptionPane.showMessageDialog(this, err, "Category Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    private void populateForm(Category c) {
        if (c == null) return;
        idField.setText(String.valueOf(c.getId()));
        nameField.setText(c.getName());
        descArea.setText(c.getDescription());
        if ("INACTIVE".equalsIgnoreCase(c.getStatus())) {
            inactiveRadio.setSelected(true);
        } else {
            activeRadio.setSelected(true);
        }
    }

    private void clearForm() {
        categoryTable.clearSelection();
        idField.setText("");
        nameField.setText("");
        descArea.setText("");
        activeRadio.setSelected(true);
    }

    private void onSave(boolean isNew) {
        String name = nameField.getText().trim();
        String desc = descArea.getText().trim();
        String status = activeRadio.isSelected() ? "ACTIVE" : "INACTIVE";

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = 0;
        if (!isNew) {
            String idStr = idField.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a category from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            id = Integer.parseInt(idStr);
        }

        categoryController.saveCategory(
                id,
                name,
                desc,
                "images/categories/placeholder.jpg",
                status,
                () -> {
                    JOptionPane.showMessageDialog(this, "Category saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                },
                err -> JOptionPane.showMessageDialog(this, err, "Save Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    private void onDelete() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a category from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int cid = Integer.parseInt(idStr);
        int confirm = JOptionPane.showConfirmDialog(this, "Deactivate category #" + cid + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            categoryController.deleteCategory(
                    cid,
                    () -> {
                        JOptionPane.showMessageDialog(this, "Category deactivated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        loadData();
                    },
                    err -> JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE)
            );
        }
    }
}
