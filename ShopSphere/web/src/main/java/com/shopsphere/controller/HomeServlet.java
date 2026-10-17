package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller serving the storefront homepage with banners, categories, and featured products.
 */
@WebServlet(urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CategoryService categoryService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.categoryService = ServiceFactory.getCategoryService();
        this.productService = ServiceFactory.getProductService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = categoryService.getActiveCategories();
        List<Product> featured = productService.getFeaturedProducts(8);
        List<Product> recent = productService.getRecentProducts(8);

        // Retrieve recently viewed products from client cookie
        List<Product> recentlyViewed = new ArrayList<>();
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("recent_products".equals(c.getName())) {
                    String[] ids = c.getValue().split("-");
                    for (String idStr : ids) {
                        try {
                            int pid = Integer.parseInt(idStr.trim());
                            Product p = productService.getProductById(pid);
                            if (p != null && "ACTIVE".equalsIgnoreCase(p.getStatus())) {
                                recentlyViewed.add(p);
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    break;
                }
            }
        }

        req.setAttribute("categories", categories);
        req.setAttribute("featuredProducts", featured);
        req.setAttribute("recentProducts", recent);
        req.setAttribute("recentlyViewed", recentlyViewed);

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
