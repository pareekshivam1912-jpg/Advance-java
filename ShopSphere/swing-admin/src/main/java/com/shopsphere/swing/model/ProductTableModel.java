package com.shopsphere.swing.model;

import com.shopsphere.model.Product;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Custom TableModel extending AbstractTableModel.
 * Provides tabular representation of Product domain entities for JTable.
 */
public class ProductTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ID", "Name", "Brand", "Category", "Price (Rs.)", "Discount %", "Stock", "Status"};
    private final Class<?>[] columnClasses = {Integer.class, String.class, String.class, String.class, BigDecimal.class, BigDecimal.class, Integer.class, String.class};
    private List<Product> products = new ArrayList<>();

    public ProductTableModel() {
    }

    public ProductTableModel(List<Product> products) {
        if (products != null) {
            this.products = new ArrayList<>(products);
        }
    }

    public void setProducts(List<Product> products) {
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
        fireTableDataChanged();
    }

    public Product getProductAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < products.size()) {
            return products.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product p = products.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getId();
            case 1: return p.getName();
            case 2: return p.getBrand();
            case 3: return p.getCategoryName();
            case 4: return p.getPrice();
            case 5: return p.getDiscountPercentage();
            case 6: return p.getStock();
            case 7: return p.getStatus();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Row selection via form controls
    }
}
