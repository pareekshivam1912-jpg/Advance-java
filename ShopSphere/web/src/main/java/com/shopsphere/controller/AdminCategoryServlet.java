package com.shopsphere.controller;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Category;
import com.shopsphere.model.User;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller managing catalog category administrative CRUD operations.
 */
@WebServlet(urlPatterns = {"/admin/categories"})
public class AdminCategoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        this.categoryService = ServiceFactory.getCategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/admin/categories");
            return;
        }

        List<Category> categories = categoryService.getAllCategories();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/admin/categories.jsp").forward(req, resp);
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
                    String description = req.getParameter("description");
                    String imageUrl = req.getParameter("imageUrl");

                    categoryService.createCategory(name, description, imageUrl);
                    session.setAttribute("flashSuccess", "Category '" + name + "' added successfully.");
                    break;
                }
                case "edit": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    String name = req.getParameter("name");
                    String description = req.getParameter("description");
                    String imageUrl = req.getParameter("imageUrl");
                    String status = req.getParameter("status");

                    categoryService.updateCategory(id, name, description, imageUrl, status);
                    session.setAttribute("flashSuccess", "Category #" + id + " updated.");
                    break;
                }
                case "delete": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    categoryService.deleteCategory(id);
                    session.setAttribute("flashSuccess", "Category #" + id + " removed.");
                    break;
                }
                default:
                    break;
            }
        } catch (ShopSphereException e) {
            session.setAttribute("flashError", e.getMessage());
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Invalid category reference.");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }
}
