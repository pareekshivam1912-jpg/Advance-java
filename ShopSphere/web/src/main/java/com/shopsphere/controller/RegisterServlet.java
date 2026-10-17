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

/**
 * Controller handling prospective customer registration.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = ServiceFactory.getUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String phone = req.getParameter("phone");

        if (password == null || !password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Passwords do not match.");
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("phone", phone);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        try {
            User newUser = userService.register(name, email, password, phone);
            HttpSession session = req.getSession(true);
            session.setAttribute("flashMessage", "Registration successful! Please sign in with your credentials.");
            session.setAttribute("flashType", "success");
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (ShopSphereException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("phone", phone);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}
