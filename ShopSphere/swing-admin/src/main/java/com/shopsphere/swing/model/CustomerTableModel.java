package com.shopsphere.swing.model;

import com.shopsphere.model.User;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Custom TableModel for Customer entities.
 */
public class CustomerTableModel extends AbstractTableModel {

    private final String[] columnNames = {"User ID", "Full Name", "Email", "Phone", "Role", "Status"};
    private List<User> users = new ArrayList<>();

    public CustomerTableModel() {
    }

    public CustomerTableModel(List<User> users) {
        if (users != null) {
            this.users = new ArrayList<>(users);
        }
    }

    public void setUsers(List<User> users) {
        this.users = users != null ? new ArrayList<>(users) : new ArrayList<>();
        fireTableDataChanged();
    }

    public User getUserAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < users.size()) {
            return users.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return users.size();
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
        User u = users.get(rowIndex);
        switch (columnIndex) {
            case 0: return u.getId();
            case 1: return u.getFullName();
            case 2: return u.getEmail();
            case 3: return u.getPhone() != null ? u.getPhone() : "-";
            case 4: return u.getRole();
            case 5: return u.getStatus();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
