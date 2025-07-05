package com.ecommerce.order.model;

public class ThresholdDiscountPromotion {
    private int threshold;
    private int discount;

    public ThresholdDiscountPromotion(int threshold, int discount) {
        this.threshold = threshold;
        this.discount = discount;
    }

    public int getThreshold() {
        return threshold;
    }

    public int getDiscount() {
        return discount;
    }

    public boolean isApplicable(int amount) {
        return amount >= threshold;
    }
}
