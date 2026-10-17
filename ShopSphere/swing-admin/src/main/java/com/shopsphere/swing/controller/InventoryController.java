package com.shopsphere.swing.controller;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.ServiceFactory;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.function.Consumer;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Inventory Controller.
 */
public class InventoryController {

    private final ProductService productService;

    public InventoryController() {
        this.productService = ServiceFactory.getProductService();
    }

    public void loadLowStock(int threshold, Consumer<List<Product>> callback, Consumer<String> onError) {
        new SwingWorker<List<Product>, Void>() {
            @Override
            protected List<Product> doInBackground() {
                return productService.getLowStockProducts(threshold);
            }

            @Override
            protected void done() {
                try {
                    callback.accept(get());
                } catch (Exception e) {
                    onError.accept("Error loading low stock items: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void restockProduct(int productId, int currentStock, int addAmount, Runnable onSuccess, Consumer<String> onError) {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                int newStock = currentStock + addAmount;
                return productService.updateStock(productId, newStock);
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        onSuccess.run();
                    } else {
                        onError.accept("Failed to update stock quantity.");
                    }
                } catch (Exception e) {
                    onError.accept("Error restocking product: " + e.getMessage());
                }
            }
        }.execute();
    }
}
