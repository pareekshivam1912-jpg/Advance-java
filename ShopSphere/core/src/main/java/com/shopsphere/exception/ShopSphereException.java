package com.shopsphere.exception;

/**
 * Custom unchecked exception for application-wide service and DAO operations.
 */
public class ShopSphereException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ShopSphereException(String message) {
        super(message);
    }

    public ShopSphereException(String message, Throwable cause) {
        super(message, cause);
    }
}
