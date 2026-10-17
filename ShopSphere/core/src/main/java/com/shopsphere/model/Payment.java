package com.shopsphere.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Domain entity representing a transaction payment record.
 */
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int orderId;
    private String paymentMethod; // COD, CARD, UPI
    private String transactionReference;
    private BigDecimal amount;
    private String paymentStatus; // PENDING, PAID, FAILED
    private Timestamp paymentDate;

    public Payment() {
        this.amount = BigDecimal.ZERO;
        this.paymentStatus = "PENDING";
    }

    public Payment(int orderId, String paymentMethod, String transactionReference, BigDecimal amount, String paymentStatus) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
        this.amount = amount != null ? amount : BigDecimal.ZERO;
        this.paymentStatus = paymentStatus != null ? paymentStatus : "PENDING";
    }

    public Payment(int id, int orderId, String paymentMethod, String transactionReference, BigDecimal amount, String paymentStatus, Timestamp paymentDate) {
        this.id = id;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
        this.amount = amount != null ? amount : BigDecimal.ZERO;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", amount=" + amount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
