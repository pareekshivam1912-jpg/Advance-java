package com.shopsphere.dao;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Address;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of AddressDAO using PreparedStatements and try-with-resources.
 */
public class AddressDAOImpl implements AddressDAO {

    @Override
    public Address findById(int id) {
        String sql = "SELECT id, user_id, full_name, phone, street_address, city, state, postal_code, is_default, created_at FROM addresses WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding address by id: " + id, e);
        }
        return null;
    }

    @Override
    public List<Address> findByUserId(int userId) {
        String sql = "SELECT id, user_id, full_name, phone, street_address, city, state, postal_code, is_default, created_at FROM addresses WHERE user_id = ? ORDER BY is_default DESC, id DESC";
        List<Address> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding addresses for user: " + userId, e);
        }
        return list;
    }

    @Override
    public Address findDefaultByUserId(int userId) {
        String sql = "SELECT id, user_id, full_name, phone, street_address, city, state, postal_code, is_default, created_at FROM addresses WHERE user_id = ? AND is_default = TRUE LIMIT 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error finding default address for user: " + userId, e);
        }
        return null;
    }

    @Override
    public int insert(Address address) {
        String sql = "INSERT INTO addresses (user_id, full_name, phone, street_address, city, state, postal_code, is_default) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, address.getUserId());
            ps.setString(2, address.getFullName());
            ps.setString(3, address.getPhone());
            ps.setString(4, address.getStreetAddress());
            ps.setString(5, address.getCity());
            ps.setString(6, address.getState());
            ps.setString(7, address.getPostalCode());
            ps.setBoolean(8, address.isDefault());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        address.setId(generatedId);
                        return generatedId;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ShopSphereException("Error inserting address for user: " + address.getUserId(), e);
        }
        return -1;
    }

    @Override
    public boolean update(Address address) {
        String sql = "UPDATE addresses SET full_name = ?, phone = ?, street_address = ?, city = ?, state = ?, postal_code = ?, is_default = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address.getFullName());
            ps.setString(2, address.getPhone());
            ps.setString(3, address.getStreetAddress());
            ps.setString(4, address.getCity());
            ps.setString(5, address.getState());
            ps.setString(6, address.getPostalCode());
            ps.setBoolean(7, address.isDefault());
            ps.setInt(8, address.getId());
            ps.setInt(9, address.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error updating address id: " + address.getId(), e);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        String sql = "DELETE FROM addresses WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error deleting address id: " + id, e);
        }
    }

    @Override
    public boolean resetDefault(int userId) {
        String sql = "UPDATE addresses SET is_default = FALSE WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new ShopSphereException("Error resetting default address for user: " + userId, e);
        }
    }

    @Override
    public boolean setDefault(int id, int userId) {
        resetDefault(userId);
        String sql = "UPDATE addresses SET is_default = TRUE WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ShopSphereException("Error setting default address for id: " + id, e);
        }
    }

    private Address mapRow(ResultSet rs) throws SQLException {
        return new Address(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("full_name"),
                rs.getString("phone"),
                rs.getString("street_address"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("postal_code"),
                rs.getBoolean("is_default"),
                rs.getTimestamp("created_at")
        );
    }
}
