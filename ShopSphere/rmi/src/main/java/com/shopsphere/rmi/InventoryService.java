package com.shopsphere.rmi;

import com.shopsphere.model.Product;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * RTU Advanced Java Syllabus Core Requirement: Java Remote Method Invocation (RMI)
 *
 * Remote Interface defining distributed inventory procedures.
 * All remote methods must declare 'throws RemoteException'.
 * Parameter and return types must be primitives or implement 'java.io.Serializable'.
 */
public interface InventoryService extends Remote {

    /**
     * Looks up and returns the product matching the specified ID across the network.
     *
     * @param id Product primary key
     * @return Serialized Product entity or null if not found
     * @throws RemoteException if network or marshaling failure occurs
     */
    Product getProduct(int id) throws RemoteException;

    /**
     * Retrieves current inventory stock for a product.
     *
     * @param productId Product primary key
     * @return Remaining stock count
     * @throws RemoteException if network or marshaling failure occurs
     */
    int getStock(int productId) throws RemoteException;

    /**
     * Updates warehouse inventory stock for a product.
     *
     * @param productId Product primary key
     * @param quantity New stock count
     * @return true if updated successfully
     * @throws RemoteException if network or marshaling failure occurs
     */
    boolean updateStock(int productId, int quantity) throws RemoteException;

    /**
     * Queries all catalog products whose stock is at or below the given threshold.
     *
     * @param threshold Low stock limit
     * @return List of matching Product entities
     * @throws RemoteException if network or marshaling failure occurs
     */
    List<Product> getLowStockProducts(int threshold) throws RemoteException;
}
