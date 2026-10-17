package com.shopsphere.swing.model;

import com.shopsphere.model.Order;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Custom TableModel for Order entities.
 */
public class OrderTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Order ID", "User ID", "Customer", "Total Amount (Rs.)", "Order Status", "Payment Status", "Date"};
    private List<Order> orders = new ArrayList<>();

    public OrderTableModel() {
    }

    public OrderTableModel(List<Order> orders) {
        if (orders != null) {
            this.orders = new ArrayList<>(orders);
        }
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders != null ? new ArrayList<>(orders) : new ArrayList<>();
        fireTableDataChanged();
    }

    public Order getOrderAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < orders.size()) {
            return orders.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return orders.size();
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
        Order o = orders.get(rowIndex);
        switch (columnIndex) {
            case 0: return o.getId();
            case 1: return o.getUserId();
            case 2: return o.getUserFullName() != null ? o.getUserFullName() : "User #" + o.getUserId();
            case 3: return o.getTotalAmount();
            case 4: return o.getOrderStatus();
            case 5: return o.getPaymentStatus();
            case 6: return o.getCreatedAt();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
