package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Domain entity representing a customer delivery address.
 */
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private String fullName;
    private String phone;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private boolean isDefault;
    private Timestamp createdAt;

    public Address() {
    }

    public Address(int userId, String fullName, String phone, String streetAddress, String city, String state, String postalCode, boolean isDefault) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.isDefault = isDefault;
    }

    public Address(int id, int userId, String fullName, String phone, String streetAddress, String city, String state, String postalCode, boolean isDefault, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedAddress() {
        return fullName + ", " + streetAddress + ", " + city + ", " + state + " - " + postalCode + " (Phone: " + phone + ")";
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
