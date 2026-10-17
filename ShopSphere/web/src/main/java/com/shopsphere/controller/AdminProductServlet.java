package com.shopsphere.controller;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
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
 * Controller managing catalog product administrative operations:
 * listing, creating, editing, soft deleting, and updating stock.
 */
@WebServlet(urlPatterns = {"/admin/products"})
public class AdminProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductService productService;
    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        this.productService = ServiceFactory.getProductService();
        this.categoryService = ServiceFactory.getCategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/admin/products");
            return;
        }

        String filter = req.getParameter("filter");
        List<Product> products;
        if ("lowStock".equals(filter)) {
            products = productService.getLowStockProducts(5);
        } else {
            products = productService.getAllProducts();
        }

        List<Category> categories = categoryService.getAllCategories();

        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.setAttribute("currentFilter", filter);
        req.getRequestDispatcher("/admin/products.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "add";
        }

        try {
            switch (action) {
                case "add": {
                    String name = req.getParameter("name");
                    String brand = req.getParameter("brand");
                    int categoryId = Integer.parseInt(req.getParameter("categoryId"));
                    String description = req.getParameter("description");
                    BigDecimal price = new BigDecimal(req.getParameter("price"));
                    BigDecimal discount = new BigDecimal(req.getParameter("discountPercentage"));
                    int stock = Integer.parseInt(req.getParameter("stock"));
                    String imageUrl = req.getParameter("imageUrl");
                    String status = req.getParameter("status");

                    Product prod = new Product(categoryId, name, brand, description, price, discount, stock, imageUrl, BigDecimal.ZERO, status);
                    productService.addProduct(prod);
                    session.setAttribute("flashSuccess", "Product '" + name + "' created successfully.");
                    break;
                }
                case "edit": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    String name = req.getParameter("name");
                    String brand = req.getParameter("brand");
                    int categoryId = Integer.parseInt(req.getParameter("categoryId"));
                    String description = req.getParameter("description");
                    BigDecimal price = new BigDecimal(req.getParameter("price"));
                    BigDecimal discount = new BigDecimal(req.getParameter("discountPercentage"));
                    int stock = Integer.parseInt(req.getParameter("stock"));
                    String imageUrl = req.getParameter("imageUrl");
                    String status = req.getParameter("status");

                    Product prod = productService.getProductById(id);
                    if (prod != null) {
                        prod.setName(name);
                        prod.setBrand(brand);
                        prod.setCategoryId(categoryId);
                        prod.setDescription(description);
                        prod.setPrice(price);
                        prod.setDiscountPercentage(discount);
                        prod.setStock(stock);
                        prod.setImageUrl(imageUrl);
                        prod.setStatus(status);
                        productService.updateProduct(prod);
                        session.setAttribute("flashSuccess", "Product #" + id + " updated successfully.");
                    }
                    break;
                }
                case "delete": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    productService.deleteProduct(id);
                    session.setAttribute("flashSuccess", "Product #" + id + " deactivated.");
                    break;
                }
                case "updateStock": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    int delta = Integer.parseInt(req.getParameter("quantityChange"));
                    productService.updateStock(id, delta);
                    session.setAttribute("flashSuccess", "Stock for product #" + id + " updated.");
                    break;
                }
                default:
                    break;
            }
        } catch (ShopSphereException e) {
            session.setAttribute("flashError", e.getMessage());
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Invalid numeric value entered.");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }
}
