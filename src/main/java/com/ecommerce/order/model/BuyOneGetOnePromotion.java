package com.ecommerce.order.model;

public class BuyOneGetOnePromotion {
    private String targetCategory;

    public BuyOneGetOnePromotion(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    public String getTargetCategory() {
        return targetCategory;
    }

    public boolean isApplicable(Product product) {
        return product.getCategory().equals(targetCategory);
    }

    public int calculateBonusQuantity(int originalQuantity) {
        return 1; // Buy one get one means you get 1 free item per product type, regardless of quantity purchased
    }
}
