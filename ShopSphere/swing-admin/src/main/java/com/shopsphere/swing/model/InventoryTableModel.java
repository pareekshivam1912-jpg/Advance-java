package com.shopsphere.swing.model;

import com.shopsphere.model.Product;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Custom TableModel for Low Stock Inventory alerts.
 */
public class InventoryTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Product ID", "Product Name", "Brand", "Category", "Current Stock", "Price (Rs.)", "Stock Alert Level"};
    private List<Product> products = new ArrayList<>();

    public InventoryTableModel() {
    }

    public InventoryTableModel(List<Product> products) {
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product p = products.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getId();
            case 1: return p.getName();
            case 2: return p.getBrand();
            case 3: return p.getCategoryName();
            case 4: return p.getStock();
            case 5: return p.getDiscountedPrice();
            case 6: return p.getStock() <= 0 ? "CRITICAL (OUT OF STOCK)" : (p.getStock() <= 5 ? "VERY LOW" : "LOW");
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
