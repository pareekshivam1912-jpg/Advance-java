package com.shopsphere.dao;

import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit and integration tests for UserDAO against MySQL.
 */
public class UserDAOTest {

    private static UserDAO userDAO;

    @BeforeAll
    public static void setUp() {
        userDAO = new UserDAOImpl();
    }

    @Test
    public void testFindById() {
        User user = userDAO.findById(1);
        assertNotNull(user, "Admin user with id 1 must exist");
        assertEquals("admin@shopsphere.com", user.getEmail());
        assertEquals("ADMIN", user.getRole());
        assertTrue(user.isAdmin());
    }

    @Test
    public void testFindByEmail() {
        User user = userDAO.findByEmail("customer@shopsphere.com");
        assertNotNull(user, "Customer user must exist");
        assertEquals("Customer One", user.getName());
        assertEquals("CUSTOMER", user.getRole());
    }

    @Test
    public void testPasswordVerificationWithHash() {
        User admin = userDAO.findByEmail("admin@shopsphere.com");
        assertNotNull(admin);
        assertTrue(PasswordUtil.verifyPassword("Admin@123", admin.getPassword()),
                "Admin password hash should verify successfully with Admin@123");
        assertFalse(PasswordUtil.verifyPassword("WrongPassword", admin.getPassword()),
                "Wrong password should fail verification");
    }

    @Test
    public void testCountCustomers() {
        int count = userDAO.countCustomers();
        assertTrue(count >= 3, "There should be at least 3 customer users in seed data");
    }

    @Test
    public void testFindAll() {
        List<User> users = userDAO.findAll();
        assertNotNull(users);
        assertTrue(users.size() >= 4, "Should find at least 4 seeded users");
    }
}
