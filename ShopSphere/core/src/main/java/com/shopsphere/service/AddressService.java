package com.shopsphere.service;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Address;

import java.util.List;

/**
 * Service managing customer delivery addresses.
 */
public class AddressService {

    private final AddressDAO addressDAO;

    public AddressService(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public List<Address> getUserAddresses(int userId) {
        return addressDAO.findByUserId(userId);
    }

    public Address getAddressById(int id) {
        return addressDAO.findById(id);
    }

    public Address getDefaultAddress(int userId) {
        return addressDAO.findDefaultByUserId(userId);
    }

    public Address addAddress(int userId, String fullName, String phone, String streetAddress, String city, String state, String postalCode, boolean isDefault) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new ShopSphereException("Recipient name is required.");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new ShopSphereException("Phone number is required.");
        }
        if (streetAddress == null || streetAddress.trim().isEmpty()) {
            throw new ShopSphereException("Street address is required.");
        }
        if (city == null || city.trim().isEmpty() || state == null || state.trim().isEmpty() || postalCode == null || postalCode.trim().isEmpty()) {
            throw new ShopSphereException("City, state, and postal code are required.");
        }

        if (isDefault) {
            addressDAO.resetDefault(userId);
        }

        Address addr = new Address(userId, fullName.trim(), phone.trim(), streetAddress.trim(), city.trim(), state.trim(), postalCode.trim(), isDefault);
        int id = addressDAO.insert(addr);
        addr.setId(id);
        return addr;
    }

    public boolean updateAddress(Address address) {
        if (address.isDefault()) {
            addressDAO.resetDefault(address.getUserId());
        }
        return addressDAO.update(address);
    }

    public boolean deleteAddress(int id, int userId) {
        return addressDAO.delete(id, userId);
    }

    public boolean setDefaultAddress(int id, int userId) {
        return addressDAO.setDefault(id, userId);
    }
}
