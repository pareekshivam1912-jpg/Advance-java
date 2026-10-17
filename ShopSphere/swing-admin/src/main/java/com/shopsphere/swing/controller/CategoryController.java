package com.shopsphere.swing.controller;

import com.shopsphere.model.Category;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ServiceFactory;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.function.Consumer;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Category Controller.
 */
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController() {
        this.categoryService = ServiceFactory.getCategoryService();
    }

    public void loadCategories(Consumer<List<Category>> callback, Consumer<String> onError) {
        new SwingWorker<List<Category>, Void>() {
            @Override
            protected List<Category> doInBackground() {
                return categoryService.getAllCategories();
            }

            @Override
            protected void done() {
                try {
                    callback.accept(get());
                } catch (Exception e) {
                    onError.accept("Error loading categories: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void saveCategory(int id, String name, String description, String imageUrl, String status,
                             Runnable onSuccess, Consumer<String> onError) {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                if (id <= 0) {
                    categoryService.createCategory(name, description, imageUrl);
                    return true;
                } else {
                    return categoryService.updateCategory(id, name, description, imageUrl, status);
                }
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        onSuccess.run();
                    } else {
                        onError.accept("Failed to save category.");
                    }
                } catch (Exception e) {
                    onError.accept("Error saving category: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void deleteCategory(int id, Runnable onSuccess, Consumer<String> onError) {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return categoryService.deleteCategory(id);
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        onSuccess.run();
                    } else {
                        onError.accept("Failed to deactivate category.");
                    }
                } catch (Exception e) {
                    onError.accept("Error: " + e.getMessage());
                }
            }
        }.execute();
    }
}
