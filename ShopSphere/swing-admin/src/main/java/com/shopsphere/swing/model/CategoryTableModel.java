package com.shopsphere.swing.model;

import com.shopsphere.model.Category;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Custom TableModel for Category entities.
 */
public class CategoryTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ID", "Name", "Description", "Status"};
    private List<Category> categories = new ArrayList<>();

    public CategoryTableModel() {
    }

    public CategoryTableModel(List<Category> categories) {
        if (categories != null) {
            this.categories = new ArrayList<>(categories);
        }
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories != null ? new ArrayList<>(categories) : new ArrayList<>();
        fireTableDataChanged();
    }

    public Category getCategoryAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < categories.size()) {
            return categories.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return categories.size();
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
        Category c = categories.get(rowIndex);
        switch (columnIndex) {
            case 0: return c.getId();
            case 1: return c.getName();
            case 2: return c.getDescription();
            case 3: return c.getStatus();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
