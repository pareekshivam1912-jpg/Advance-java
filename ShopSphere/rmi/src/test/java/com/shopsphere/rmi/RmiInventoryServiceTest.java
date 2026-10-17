package com.shopsphere.rmi;

import com.shopsphere.model.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test verifying RMI distributed calls against in-process registry.
 */
public class RmiInventoryServiceTest {

    private static final int TEST_PORT = 1098;
    private static Registry registry;
    private static InventoryServiceImpl serviceImpl;

    @BeforeAll
    static void setupRmi() throws Exception {
        registry = LocateRegistry.createRegistry(TEST_PORT);
        serviceImpl = new InventoryServiceImpl(0);
        registry.rebind("InventoryService", serviceImpl);
    }

    @AfterAll
    static void tearDownRmi() {
        try {
            if (registry != null) {
                registry.unbind("InventoryService");
            }
        } catch (Exception ignored) {
        }
    }

    @Test
    void testRemoteMethodInvocations() throws Exception {
        Registry clientRegistry = LocateRegistry.getRegistry("localhost", TEST_PORT);
        InventoryService service = (InventoryService) clientRegistry.lookup("InventoryService");
        assertNotNull(service);

        // 1. getProduct
        Product product = service.getProduct(1);
        assertNotNull(product);
        assertEquals(1, product.getId());

        // 2. getStock
        int stock = service.getStock(1);
        assertTrue(stock >= 0);

        // 3. updateStock and restore
        boolean updated = service.updateStock(1, stock + 1);
        assertTrue(updated);
        assertEquals(stock + 1, service.getStock(1));

        // Restore original
        service.updateStock(1, stock);
        assertEquals(stock, service.getStock(1));

        // 4. getLowStockProducts
        List<Product> lowStock = service.getLowStockProducts(50);
        assertNotNull(lowStock);
        assertFalse(lowStock.isEmpty());
    }
}
