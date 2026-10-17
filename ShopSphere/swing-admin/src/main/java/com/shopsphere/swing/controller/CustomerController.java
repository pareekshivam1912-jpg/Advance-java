package com.shopsphere.swing.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.ServiceFactory;
import com.shopsphere.service.UserService;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.function.Consumer;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Customer Controller.
 */
public class CustomerController {

    private final UserService userService;

    public CustomerController() {
        this.userService = ServiceFactory.getUserService();
    }

    public void loadCustomers(Consumer<List<User>> callback, Consumer<String> onError) {
        new SwingWorker<List<User>, Void>() {
            @Override
            protected List<User> doInBackground() {
                return userService.getAllUsers();
            }

            @Override
            protected void done() {
                try {
                    callback.accept(get());
                } catch (Exception e) {
                    onError.accept("Error loading customers: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void toggleCustomerStatus(int userId, String currentStatus, Runnable onSuccess, Consumer<String> onError) {
        String newStatus = "ACTIVE".equalsIgnoreCase(currentStatus) ? "INACTIVE" : "ACTIVE";
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return userService.toggleUserStatus(userId, newStatus);
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        onSuccess.run();
                    } else {
                        onError.accept("Failed to toggle status.");
                    }
                } catch (Exception e) {
                    onError.accept("Error: " + e.getMessage());
                }
            }
        }.execute();
    }
}
