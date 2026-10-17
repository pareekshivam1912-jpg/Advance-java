package com.shopsphere.dao;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Product;
import com.shopsphere.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of ProductDAO using PreparedStatements and try-with-resources.
 */
public class ProductDAOImpl implements ProductDAO {

    private static final String BASE_SELECT =
            "SELECT p.id, p.category_id, c.name AS category_name, p.name, p.brand, p.description, " +
            "p.price, p.discount_percentage, p.stock, p.image_url, p.rating, p.status, p.created_at " +
            "FROM products p " +
            "JOIN categories c ON p.category_id = c.id ";

    @Override
    public Product findById(int id) {
        String sql = BASE_SELECT + "WHERE p.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding product by id: " + id, e);
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        String sql = BASE_SELECT + "ORDER BY p.id DESC";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding all products", e);
        }
        return list;
    }

    @Override
    public List<Product> findFeatured(int limit) {
        String sql = BASE_SELECT + "WHERE p.status = 'ACTIVE' ORDER BY p.rating DESC, p.id DESC LIMIT ?";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding featured products", e);
        }
        return list;
    }

    @Override
    public List<Product> findRecent(int limit) {
        String sql = BASE_SELECT + "WHERE p.status = 'ACTIVE' ORDER BY p.created_at DESC, p.id DESC LIMIT ?";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding recent products", e);
        }
        return list;
    }

    @Override
    public List<Product> findByCategory(int categoryId, int limit) {
        String sql = BASE_SELECT + "WHERE p.category_id = ? AND p.status = 'ACTIVE' ORDER BY p.id DESC LIMIT ?";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding products by category: " + categoryId, e);
        }
        return list;
    }

    @Override
    public List<Product> search(String keyword, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, int page, int pageSize) {
        StringBuilder sql = new StringBuilder(BASE_SELECT).append("WHERE p.status = 'ACTIVE' ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ? OR LOWER(p.brand) LIKE ?) ");
            String kwParam = "%" + keyword.trim().toLowerCase() + "%";
            params.add(kwParam);
            params.add(kwParam);
            params.add(kwParam);
        }

        if (categoryId != null && categoryId > 0) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }

        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) >= 0) {
            sql.append("AND p.price >= ? ");
            params.add(minPrice);
        }

        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) > 0) {
            sql.append("AND p.price <= ? ");
            params.add(maxPrice);
        }

        if ("price_asc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price ASC ");
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price DESC ");
        } else if ("rating".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.rating DESC ");
        } else {
            sql.append("ORDER BY p.id DESC ");
        }

        sql.append("LIMIT ? OFFSET ?");
        params.add(pageSize > 0 ? pageSize : 12);
        int offset = Math.max(0, (page - 1) * pageSize);
        params.add(offset);

        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error searching products", e);
        }
        return list;
    }

    @Override
    public int countSearch(String keyword, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products p WHERE p.status = 'ACTIVE' ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ? OR LOWER(p.brand) LIKE ?) ");
            String kwParam = "%" + keyword.trim().toLowerCase() + "%";
            params.add(kwParam);
            params.add(kwParam);
            params.add(kwParam);
        }

        if (categoryId != null && categoryId > 0) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }

        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) >= 0) {
            sql.append("AND p.price >= ? ");
            params.add(minPrice);
        }

        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) > 0) {
            sql.append("AND p.price <= ? ");
            params.add(maxPrice);
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error counting products for search", e);
        }
        return 0;
    }

    @Override
    public List<Product> findLowStock(int threshold) {
        String sql = BASE_SELECT + "WHERE p.stock <= ? AND p.status = 'ACTIVE' ORDER BY p.stock ASC";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding low stock products threshold: " + threshold, e);
        }
        return list;
    }

    @Override
    public int insert(Product product) {
        String sql = "INSERT INTO products (category_id, name, brand, description, price, discount_percentage, stock, image_url, rating, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setBigDecimal(5, product.getPrice());
            ps.setBigDecimal(6, product.getDiscountPercentage());
            ps.setInt(7, product.getStock());
            ps.setString(8, product.getImageUrl());
            ps.setBigDecimal(9, product.getRating());
            ps.setString(10, product.getStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        product.setId(generatedId);
                        return generatedId;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error inserting product: " + product.getName(), e);
        }
        return -1;
    }

    @Override
    public boolean update(Product product) {
        String sql = "UPDATE products SET category_id = ?, name = ?, brand = ?, description = ?, " +
                     "price = ?, discount_percentage = ?, stock = ?, image_url = ?, status = ? " +
                     "WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setBigDecimal(5, product.getPrice());
            ps.setBigDecimal(6, product.getDiscountPercentage());
            ps.setInt(7, product.getStock());
            ps.setString(8, product.getImageUrl());
            ps.setString(9, product.getStatus());
            ps.setInt(10, product.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating product: " + product.getId(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE products SET status = 'INACTIVE' WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error deleting product id: " + id, e);
        }
    }

    @Override
    public boolean updateStock(int productId, int newStock) {
        String sql = "UPDATE products SET stock = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating stock for product id: " + productId, e);
        }
    }

    @Override
    public boolean updateStock(Connection conn, int productId, int quantityDelta) {
        String sql = "UPDATE products SET stock = stock + ? WHERE id = ? AND (stock + ?) >= 0";
        if (conn == null) {
            try (Connection c = DBUtil.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, quantityDelta);
                ps.setInt(2, productId);
                ps.setInt(3, quantityDelta);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new ShopSphereException("Error updating stock delta for product id: " + productId, e);
            }
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantityDelta);
            ps.setInt(2, productId);
            ps.setInt(3, quantityDelta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating stock delta for product id: " + productId, e);
        }
    }

    @Override
    public boolean updateRating(int productId, BigDecimal newRating) {
        String sql = "UPDATE products SET rating = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, newRating);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating rating for product id: " + productId, e);
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getInt("category_id"),
                rs.getString("category_name"),
                rs.getString("name"),
                rs.getString("brand"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                rs.getBigDecimal("discount_percentage"),
                rs.getInt("stock"),
                rs.getString("image_url"),
                rs.getBigDecimal("rating"),
                rs.getString("status"),
                rs.getTimestamp("created_at")
        );
    }
}
