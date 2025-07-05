package com.ecommerce.order.model;

public class FinalOrderItem {
    private Product product;
    private int quantity;

    public FinalOrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return product.getName();
    }
}
