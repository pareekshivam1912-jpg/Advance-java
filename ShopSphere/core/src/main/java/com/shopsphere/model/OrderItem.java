package com.shopsphere.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Domain entity representing an individual item within an order.
 * Implements Serializable.
 */
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int orderId;
    private int productId;
    private BigDecimal pricePerUnit;
    private int quantity;
    private BigDecimal subtotal;
    private Product product; // Attached product details

    public OrderItem() {
        this.pricePerUnit = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
    }

    public OrderItem(int orderId, int productId, BigDecimal pricePerUnit, int quantity, BigDecimal subtotal) {
        this.orderId = orderId;
        this.productId = productId;
        this.pricePerUnit = pricePerUnit != null ? pricePerUnit : BigDecimal.ZERO;
        this.quantity = quantity;
        this.subtotal = subtotal != null ? subtotal : BigDecimal.ZERO;
    }

    public OrderItem(int id, int orderId, int productId, BigDecimal pricePerUnit, int quantity, BigDecimal subtotal) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.pricePerUnit = pricePerUnit != null ? pricePerUnit : BigDecimal.ZERO;
        this.quantity = quantity;
        this.subtotal = subtotal != null ? subtotal : BigDecimal.ZERO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return product != null && product.getName() != null ? product.getName() : "Product #" + productId;
    }

    public BigDecimal getPrice() {
        return pricePerUnit;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", pricePerUnit=" + pricePerUnit +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }
}
