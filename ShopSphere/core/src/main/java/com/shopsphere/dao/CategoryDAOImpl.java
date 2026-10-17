package com.shopsphere.dao;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Category;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of CategoryDAO using PreparedStatements and try-with-resources.
 */
public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public Category findById(int id) {
        String sql = "SELECT id, name, description, image_url, status, created_at FROM categories WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding category by id: " + id, e);
        }
        return null;
    }

    @Override
    public Category findByName(String name) {
        String sql = "SELECT id, name, description, image_url, status, created_at FROM categories WHERE name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding category by name: " + name, e);
        }
        return null;
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT id, name, description, image_url, status, created_at FROM categories ORDER BY id ASC";
        List<Category> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding all categories", e);
        }
        return list;
    }

    @Override
    public List<Category> findActive() {
        String sql = "SELECT id, name, description, image_url, status, created_at FROM categories WHERE status = 'ACTIVE' ORDER BY id ASC";
        List<Category> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding active categories", e);
        }
        return list;
    }

    @Override
    public int insert(Category category) {
        String sql = "INSERT INTO categories (name, description, image_url, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setString(3, category.getImageUrl());
            ps.setString(4, category.getStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        category.setId(generatedId);
                        return generatedId;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error inserting category: " + category.getName(), e);
        }
        return -1;
    }

    @Override
    public boolean update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ?, image_url = ?, status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setString(3, category.getImageUrl());
            ps.setString(4, category.getStatus());
            ps.setInt(5, category.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating category: " + category.getId(), e);
        }
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE categories SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating category status for id: " + id, e);
        }
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("image_url"),
                rs.getString("status"),
                rs.getTimestamp("created_at")
        );
    }
}
