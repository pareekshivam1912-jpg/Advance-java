package com.shopsphere.controller;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.User;
import com.shopsphere.service.ServiceFactory;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller managing customer accounts and status toggling for administrators.
 */
@WebServlet(urlPatterns = {"/admin/users"})
public class AdminUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = ServiceFactory.getUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/admin/users");
            return;
        }

        List<User> users = userService.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/admin/users.jsp").forward(req, resp);
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
        if ("toggleStatus".equals(action)) {
            try {
                int targetId = Integer.parseInt(req.getParameter("userId"));
                String newStatus = req.getParameter("newStatus");

                if (targetId == user.getId()) {
                    session.setAttribute("flashError", "You cannot deactivate your own administrative account.");
                } else {
                    userService.toggleUserStatus(targetId, newStatus);
                    session.setAttribute("flashSuccess", "User account status updated to " + newStatus + ".");
                }
            } catch (ShopSphereException e) {
                session.setAttribute("flashError", e.getMessage());
            } catch (NumberFormatException e) {
                session.setAttribute("flashError", "Invalid user reference.");
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
