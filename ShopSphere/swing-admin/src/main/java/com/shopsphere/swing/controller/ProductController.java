package com.shopsphere.swing.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.ServiceFactory;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.function.Consumer;

/**
 * RTU Advanced Java Syllabus: Swing MVC - Product Controller.
 * Decouples Swing UI views from DAO/Service layers, executing operations via SwingWorker.
 */
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController() {
        this.productService = ServiceFactory.getProductService();
        this.categoryService = ServiceFactory.getCategoryService();
    }

    public void loadProducts(Consumer<List<Product>> callback, Consumer<String> onError) {
        new SwingWorker<List<Product>, Void>() {
            @Override
            protected List<Product> doInBackground() {
                return productService.getAllProducts();
            }

            @Override
            protected void done() {
                try {
                    callback.accept(get());
                } catch (Exception e) {
                    onError.accept("Error loading products: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void loadCategories(Consumer<List<Category>> callback, Consumer<String> onError) {
        new SwingWorker<List<Category>, Void>() {
            @Override
            protected List<Category> doInBackground() {
                return categoryService.getAllCategories();
            }

            @Override
            protected void done() {
                try {
                    callback.accept(get());
                } catch (Exception e) {
                    onError.accept("Error loading categories: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void saveProduct(Product product, boolean isNew, Runnable onSuccess, Consumer<String> onError) {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                if (isNew) {
                    productService.addProduct(product);
                    return true;
                } else {
                    return productService.updateProduct(product);
                }
            }

            @Override
            protected void done() {
                try {
                    boolean result = get();
                    if (result) {
                        onSuccess.run();
                    } else {
                        onError.accept("Failed to save product in database.");
                    }
                } catch (Exception e) {
                    onError.accept("Error saving product: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void deleteProduct(int productId, Runnable onSuccess, Consumer<String> onError) {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return productService.deleteProduct(productId);
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        onSuccess.run();
                    } else {
                        onError.accept("Could not delete product.");
                    }
                } catch (Exception e) {
                    onError.accept("Error deleting product: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void updateStock(int productId, int newStock, Runnable onSuccess, Consumer<String> onError) {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return productService.updateStock(productId, newStock);
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        onSuccess.run();
                    } else {
                        onError.accept("Could not update stock.");
                    }
                } catch (Exception e) {
                    onError.accept("Error updating stock: " + e.getMessage());
                }
            }
        }.execute();
    }
}
