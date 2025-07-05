package com.ecommerce.order.model;

import java.util.List;

public class Double11Promotion {
    private double discountRate = 0.2; // 20% discount
    private int minQuantityPerProduct = 10; // Every 10 pieces of same product
    
    public boolean isApplicable(List<OrderItem> items) {
        // Always applicable - the calculation logic will handle the discount
        return true;
    }
    
    public int calculateDiscount(List<OrderItem> items) {
        int totalDiscount = 0;
        
        for (OrderItem item : items) {
            int quantity = item.getQuantity();
            int unitPrice = item.getProduct().getUnitPrice();
            
            // Calculate how many complete sets of 10 pieces
            int discountableSets = quantity / minQuantityPerProduct;
            
            if (discountableSets > 0) {
                // Calculate discount for complete sets
                int discountableAmount = discountableSets * minQuantityPerProduct * unitPrice;
                totalDiscount += (int) (discountableAmount * discountRate);
            }
        }
        
        return totalDiscount;
    }
    
    public double getDiscountRate() {
        return discountRate;
    }
    
    public int getMinQuantityPerProduct() {
        return minQuantityPerProduct;
    }
}
