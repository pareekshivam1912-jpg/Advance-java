package com.shopsphere.dao;

import com.shopsphere.model.Address;

import java.util.List;

/**
 * Data Access Object interface for Address entities.
 */
public interface AddressDAO {
    Address findById(int id);
    List<Address> findByUserId(int userId);
    Address findDefaultByUserId(int userId);
    int insert(Address address);
    boolean update(Address address);
    boolean delete(int id, int userId);
    boolean resetDefault(int userId);
    boolean setDefault(int id, int userId);
}
