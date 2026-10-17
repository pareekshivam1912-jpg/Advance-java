package com.shopsphere.dao;

import com.shopsphere.model.Category;

import java.util.List;

/**
 * Data Access Object interface for Category entities.
 */
public interface CategoryDAO {
    Category findById(int id);
    Category findByName(String name);
    List<Category> findAll();
    List<Category> findActive();
    int insert(Category category);
    boolean update(Category category);
    boolean updateStatus(int id, String status);
}
