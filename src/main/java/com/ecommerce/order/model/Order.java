package com.ecommerce.order.model;

import java.util.List;
import java.util.ArrayList;

public class Order {
    private List<OrderItem> items;
    private List<FinalOrderItem> finalItems;
    private int originalAmount;
    private int discount;
    private int totalAmount;

    public Order() {
        this.items = new ArrayList<>();
        this.finalItems = new ArrayList<>();
        this.originalAmount = 0;
        this.discount = 0;
        this.totalAmount = 0;
    }

    // Getters and setters for original items
    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    // Getters and setters for final items (what customer receives)
    public List<FinalOrderItem> getFinalItems() {
        return finalItems;
    }

    public void addFinalItem(FinalOrderItem item) {
        this.finalItems.add(item);
    }

    public int getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(int originalAmount) {
        this.originalAmount = originalAmount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}
