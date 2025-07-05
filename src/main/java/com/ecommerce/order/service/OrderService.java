package com.ecommerce.order.service;

import com.ecommerce.order.model.*;

import java.util.List;

public class OrderService {
    
    private ThresholdDiscountPromotion thresholdDiscountPromotion;
    private BuyOneGetOnePromotion buyOneGetOnePromotion;
    
    public void setThresholdDiscountPromotion(ThresholdDiscountPromotion promotion) {
        this.thresholdDiscountPromotion = promotion;
    }
    
    public void setBuyOneGetOnePromotion(BuyOneGetOnePromotion promotion) {
        this.buyOneGetOnePromotion = promotion;
    }
    
    public Order processOrder(List<OrderItem> items) {
        Order order = new Order();
        
        // Add all items to order
        for (OrderItem item : items) {
            order.addItem(item);
        }
        
        // Apply buy-one-get-one promotion and create final items
        for (OrderItem item : items) {
            int finalQuantity = item.getQuantity();
            if (buyOneGetOnePromotion != null && buyOneGetOnePromotion.isApplicable(item.getProduct())) {
                finalQuantity += buyOneGetOnePromotion.calculateBonusQuantity(item.getQuantity());
            }
            order.addFinalItem(new FinalOrderItem(item.getProduct(), finalQuantity));
        }
        
        // Calculate original amount
        int originalAmount = calculateOriginalAmount(items);
        order.setOriginalAmount(originalAmount);
        
        // Apply threshold discount if applicable
        int discount = 0;
        if (thresholdDiscountPromotion != null && thresholdDiscountPromotion.isApplicable(originalAmount)) {
            discount = thresholdDiscountPromotion.getDiscount();
        }
        
        order.setDiscount(discount);
        order.setTotalAmount(originalAmount - discount);
        
        return order;
    }
    
    private int calculateOriginalAmount(List<OrderItem> items) {
        int total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
}
