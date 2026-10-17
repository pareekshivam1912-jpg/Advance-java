package com.shopsphere.service;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;

import java.util.List;

/**
 * Service managing user authentication, registration, and profile administration.
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User authenticate(String email, String plainPassword) {
        if (email == null || plainPassword == null) {
            throw new ShopSphereException("Email and password are required.");
        }
        User user = userDAO.findByEmail(email.trim().toLowerCase());
        if (user == null) {
            throw new ShopSphereException("Invalid email or password.");
        }
        if (!user.isActive()) {
            throw new ShopSphereException("Your account is deactivated. Please contact support.");
        }
        if (!PasswordUtil.verifyPassword(plainPassword, user.getPassword())) {
            throw new ShopSphereException("Invalid email or password.");
        }
        return user;
    }

    public User register(String name, String email, String plainPassword, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new ShopSphereException("Full name is required.");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ShopSphereException("A valid email address is required.");
        }
        if (plainPassword == null || plainPassword.length() < 6) {
            throw new ShopSphereException("Password must be at least 6 characters long.");
        }
        String normalizedEmail = email.trim().toLowerCase();
        if (userDAO.findByEmail(normalizedEmail) != null) {
            throw new ShopSphereException("An account with this email address already exists.");
        }

        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        User user = new User(name.trim(), normalizedEmail, hashedPassword, phone != null ? phone.trim() : "", "CUSTOMER");
        int id = userDAO.insert(user);
        user.setId(id);
        return user;
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new ShopSphereException("User not found.");
        }
        if (!PasswordUtil.verifyPassword(oldPassword, user.getPassword())) {
            throw new ShopSphereException("Incorrect current password.");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new ShopSphereException("New password must be at least 6 characters long.");
        }
        String newHash = PasswordUtil.hashPassword(newPassword);
        return userDAO.updatePassword(userId, newHash);
    }

    public boolean updateProfile(int userId, String name, String phone) {
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new ShopSphereException("User not found.");
        }
        user.setName(name.trim());
        user.setPhone(phone != null ? phone.trim() : "");
        return userDAO.update(user);
    }

    public boolean toggleUserStatus(int userId, String newStatus) {
        return userDAO.updateStatus(userId, newStatus);
    }

    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public int getCustomerCount() {
        return userDAO.countCustomers();
    }
}
