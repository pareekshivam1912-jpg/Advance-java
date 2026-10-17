package com.shopsphere.rmi;

import com.shopsphere.model.Product;
import com.shopsphere.service.LocalInventoryService;
import com.shopsphere.service.ServiceFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Logger;

/**
 * RTU Advanced Java Syllabus Core Requirement: Java Remote Method Invocation (RMI)
 *
 * Remote Object Implementation.
 * Extends UnicastRemoteObject to establish a point-to-point remote communication channel.
 * Delegates inventory queries to shared LocalInventoryService / DAO / MySQL database.
 */
public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(InventoryServiceImpl.class.getName());

    private final LocalInventoryService localService;

    public InventoryServiceImpl() throws RemoteException {
        super();
        this.localService = ServiceFactory.getLocalInventoryService();
    }

    public InventoryServiceImpl(int port) throws RemoteException {
        super(port);
        this.localService = ServiceFactory.getLocalInventoryService();
    }

    @Override
    public Product getProduct(int id) throws RemoteException {
        LOGGER.info("[RMI Server] Remote invocation: getProduct(id=" + id + ")");
        return localService.getProduct(id);
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        LOGGER.info("[RMI Server] Remote invocation: getStock(productId=" + productId + ")");
        return localService.getStock(productId);
    }

    @Override
    public boolean updateStock(int productId, int quantity) throws RemoteException {
        LOGGER.info("[RMI Server] Remote invocation: updateStock(productId=" + productId + ", quantity=" + quantity + ")");
        return localService.updateStock(productId, quantity);
    }

    @Override
    public List<Product> getLowStockProducts(int threshold) throws RemoteException {
        LOGGER.info("[RMI Server] Remote invocation: getLowStockProducts(threshold=" + threshold + ")");
        return localService.getLowStockProducts(threshold);
    }
}
