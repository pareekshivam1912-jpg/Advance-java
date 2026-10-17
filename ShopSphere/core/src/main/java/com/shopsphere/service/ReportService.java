package com.shopsphere.service;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.UserDAO;

import java.math.BigDecimal;

/**
 * Service providing aggregated management KPI metrics and sales summaries.
 */
public class ReportService {

    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;

    public ReportService(OrderDAO orderDAO, UserDAO userDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.productDAO = productDAO;
    }

    public BigDecimal getTotalSales() {
        return orderDAO.getTotalSales();
    }

    public int getTotalOrders() {
        return orderDAO.countAll();
    }

    public int getTotalCustomers() {
        return userDAO.countCustomers();
    }

    public int getLowStockCount(int threshold) {
        return productDAO.findLowStock(threshold).size();
    }
}
