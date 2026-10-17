package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.ReportService;
import com.shopsphere.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller serving the administrative dashboard with KPI metrics, low-stock warnings, and recent orders.
 */
@WebServlet(urlPatterns = {"/admin/dashboard", "/admin"})
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ReportService reportService;
    private OrderService orderService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.reportService = ServiceFactory.getReportService();
        this.orderService = ServiceFactory.getOrderService();
        this.productService = ServiceFactory.getProductService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/admin/dashboard");
            return;
        }

        BigDecimal totalSales = reportService.getTotalSales();
        int totalOrders = reportService.getTotalOrders();
        int totalCustomers = reportService.getTotalCustomers();
        int lowStockCount = reportService.getLowStockCount(5);

        List<Order> allOrders = orderService.getAllOrders();
        List<Order> recentOrders = allOrders.size() > 8 ? allOrders.subList(0, 8) : allOrders;

        List<Product> lowStockProducts = productService.getLowStockProducts(5);

        req.setAttribute("totalSales", totalSales != null ? totalSales : BigDecimal.ZERO);
        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("totalCustomers", totalCustomers);
        req.setAttribute("lowStockCount", lowStockCount);
        req.setAttribute("recentOrders", recentOrders);
        req.setAttribute("lowStockProducts", lowStockProducts);

        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
    }
}
