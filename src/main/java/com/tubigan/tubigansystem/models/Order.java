package com.tubigan.tubigansystem.models;

public class Order {
    private int id;
    private int customerId;
    private int gallons;
    private String orderType;
    private double amount;
    private String paymentMethod;
    private String status;
    private String date;

    public Order(int id, int customerId, int gallons, String orderType, double amount, String paymentMethod, String status, String date) {
        this.id = id;
        this.customerId = customerId;
        this.gallons = gallons;
        this.orderType = orderType;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.date = date;
    }

    // Getters
    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public int getGallons() { return gallons; }
    public String getOrderType() { return orderType; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public String getDate() { return date; }
}