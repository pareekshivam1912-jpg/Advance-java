package com.shopsphere.dao;

import com.shopsphere.model.User;

import java.util.List;

/**
 * Data Access Object interface for User entities.
 */
public interface UserDAO {
    User findById(int id);
    User findByEmail(String email);
    List<User> findAll();
    int insert(User user);
    boolean update(User user);
    boolean updateStatus(int id, String status);
    boolean updatePassword(int id, String newPassword);
    int countCustomers();
}
