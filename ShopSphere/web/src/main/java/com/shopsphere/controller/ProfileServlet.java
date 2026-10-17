package com.shopsphere.controller;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Address;
import com.shopsphere.model.User;
import com.shopsphere.service.AddressService;
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
 * Controller managing customer profile details, password updates, and saved address books.
 */
@WebServlet(urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService userService;
    private AddressService addressService;

    @Override
    public void init() throws ServletException {
        this.userService = ServiceFactory.getUserService();
        this.addressService = ServiceFactory.getAddressService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/profile");
            return;
        }

        User freshUser = userService.getUserById(user.getId());
        List<Address> addresses = addressService.getUserAddresses(user.getId());

        req.setAttribute("profileUser", freshUser);
        req.setAttribute("addresses", addresses);
        req.getRequestDispatcher("/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "updateProfile";
        }

        try {
            switch (action) {
                case "updateProfile": {
                    String name = req.getParameter("name");
                    String phone = req.getParameter("phone");

                    userService.updateProfile(user.getId(), name, phone);
                    User updated = userService.getUserById(user.getId());
                    session.setAttribute("user", updated);
                    session.setAttribute("flashSuccess", "Profile updated successfully.");
                    break;
                }
                case "changePassword": {
                    String oldPassword = req.getParameter("oldPassword");
                    String newPassword = req.getParameter("newPassword");
                    String confirmPassword = req.getParameter("confirmPassword");

                    if (!newPassword.equals(confirmPassword)) {
                        throw new ShopSphereException("New passwords do not match.");
                    }

                    userService.changePassword(user.getId(), oldPassword, newPassword);
                    session.setAttribute("flashSuccess", "Password changed successfully.");
                    break;
                }
                case "addAddress": {
                    String fullName = req.getParameter("fullName");
                    String phone = req.getParameter("phone");
                    String street = req.getParameter("streetAddress");
                    String city = req.getParameter("city");
                    String state = req.getParameter("state");
                    String postalCode = req.getParameter("postalCode");
                    boolean isDefault = "true".equalsIgnoreCase(req.getParameter("isDefault")) || req.getParameter("isDefault") != null;

                    addressService.addAddress(user.getId(), fullName, phone, street, city, state, postalCode, isDefault);
                    session.setAttribute("flashSuccess", "Address added successfully.");
                    break;
                }
                case "deleteAddress": {
                    int addressId = Integer.parseInt(req.getParameter("addressId"));
                    addressService.deleteAddress(addressId, user.getId());
                    session.setAttribute("flashSuccess", "Address deleted successfully.");
                    break;
                }
                case "setDefaultAddress": {
                    int addressId = Integer.parseInt(req.getParameter("addressId"));
                    addressService.setDefaultAddress(addressId, user.getId());
                    session.setAttribute("flashSuccess", "Default address updated.");
                    break;
                }
                default:
                    break;
            }
        } catch (ShopSphereException e) {
            session.setAttribute("flashError", e.getMessage());
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Invalid numerical parameters.");
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
