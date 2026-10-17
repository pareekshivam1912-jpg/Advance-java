package com.shopsphere.service;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Category;

import java.util.List;

/**
 * Service managing product categories.
 */
public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Category> getActiveCategories() {
        return categoryDAO.findActive();
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryDAO.findById(id);
    }

    public Category createCategory(String name, String description, String imageUrl) {
        if (name == null || name.trim().isEmpty()) {
            throw new ShopSphereException("Category name is required.");
        }
        if (categoryDAO.findByName(name.trim()) != null) {
            throw new ShopSphereException("A category with this name already exists.");
        }
        Category c = new Category(name.trim(), description, imageUrl);
        int id = categoryDAO.insert(c);
        c.setId(id);
        return c;
    }

    public boolean updateCategory(int id, String name, String description, String imageUrl, String status) {
        Category c = categoryDAO.findById(id);
        if (c == null) {
            throw new ShopSphereException("Category not found.");
        }
        c.setName(name.trim());
        c.setDescription(description);
        c.setImageUrl(imageUrl);
        c.setStatus(status);
        return categoryDAO.update(c);
    }

    public boolean deleteCategory(int id) {
        return categoryDAO.updateStatus(id, "INACTIVE");
    }

    public boolean toggleCategoryStatus(int id, String status) {
        return categoryDAO.updateStatus(id, status);
    }
}
