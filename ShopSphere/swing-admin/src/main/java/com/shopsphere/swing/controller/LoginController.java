package com.shopsphere.swing.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.ServiceFactory;
import com.shopsphere.service.UserService;

import javax.swing.SwingWorker;
import java.util.function.Consumer;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Login Controller.
 * Handles authentication asynchronously off the Event Dispatch Thread (EDT) using SwingWorker.
 */
public class LoginController {

    private final UserService userService;

    public LoginController() {
        this.userService = ServiceFactory.getUserService();
    }

    public void authenticate(String email, String password, Consumer<User> onSuccess, Consumer<String> onError) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            onError.accept("Please enter both email and password.");
            return;
        }

        new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() {
                return userService.authenticate(email.trim(), password);
            }

            @Override
            protected void done() {
                try {
                    User user = get();
                    if (user == null) {
                        onError.accept("Invalid email or password.");
                    } else if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                        onError.accept("Access Denied: Account '" + user.getEmail() + "' is not an administrator.");
                    } else if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
                        onError.accept("Account is suspended or deactivated.");
                    } else {
                        onSuccess.accept(user);
                    }
                } catch (Exception e) {
                    onError.accept("Authentication error: " + e.getMessage());
                }
            }
        }.execute();
    }
}
