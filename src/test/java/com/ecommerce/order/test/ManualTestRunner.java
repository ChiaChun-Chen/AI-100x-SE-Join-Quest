package com.ecommerce.order.test;

import com.ecommerce.order.model.*;
import com.ecommerce.order.service.OrderService;
import java.util.ArrayList;
import java.util.List;

public class ManualTestRunner {
    
    public static void main(String[] args) {
        System.out.println("=== Running Manual BDD Test ===");
        testSingleProductWithoutPromotions();
        testThresholdDiscount();
        testBuyOneGetOneForCosmetics();
        testBuyOneGetOneSameProductTwice();
        testBuyOneGetOneMixedCategories();
        testMultiplePromotionsStacked();
    }
    
    public static void testSingleProductWithoutPromotions() {
        System.out.println("\nScenario: Single product without promotions");
        
        // Given no promotions are applied
        System.out.println("Given: no promotions are applied");
        
        // When a customer places an order with T-shirt, quantity 1, unitPrice 500
        System.out.println("When: a customer places an order with T-shirt, quantity 1, unitPrice 500");
        
        Product tshirt = new Product("T-shirt", "apparel", 500);
        OrderItem item = new OrderItem(tshirt, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        
        OrderService orderService = new OrderService();
        Order order = orderService.processOrder(items);
        
        // Then the order summary should have totalAmount 500
        System.out.println("Then: the order summary should have totalAmount 500");
        System.out.println("Actual totalAmount: " + order.getTotalAmount());
        
        // And the customer should receive T-shirt, quantity 1
        System.out.println("And: the customer should receive T-shirt, quantity 1");
        System.out.println("Actual items count: " + order.getItems().size());
        if (!order.getItems().isEmpty()) {
            OrderItem actualItem = order.getItems().get(0);
            System.out.println("Actual product: " + actualItem.getProduct().getName() + 
                             ", quantity: " + actualItem.getQuantity());
        }
        
        // Check test result
        boolean testPassed = (order.getTotalAmount() == 500) && 
                           (order.getItems().size() == 1) &&
                           (!order.getItems().isEmpty() && 
                            order.getItems().get(0).getProduct().getName().equals("T-shirt") &&
                            order.getItems().get(0).getQuantity() == 1);
        
        System.out.println("Test Result: " + (testPassed ? "PASS" : "FAIL"));
        
        if (!testPassed) {
            System.out.println("Expected: totalAmount=500, items=1, productName=T-shirt, quantity=1");
            System.out.println("Actual: totalAmount=" + order.getTotalAmount() + 
                             ", items=" + order.getItems().size());
        }
    }
    
    public static void testThresholdDiscount() {
        System.out.println("\nScenario: Threshold discount applies when subtotal reaches 1000");
        
        // Given the threshold discount promotion is configured with threshold 1000, discount 100
        System.out.println("Given: the threshold discount promotion is configured (threshold: 1000, discount: 100)");
        
        // When a customer places an order with T-shirt(2, 500) and Pants(1, 600)
        System.out.println("When: a customer places an order with T-shirt(2, 500) and Pants(1, 600)");
        
        Product tshirt = new Product("T-shirt", "apparel", 500);
        Product pants = new Product("Pants", "apparel", 600);
        OrderItem item1 = new OrderItem(tshirt, 2);
        OrderItem item2 = new OrderItem(pants, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        
        OrderService orderService = new OrderService();
        // Set threshold discount promotion: threshold 1000, discount 100
        ThresholdDiscountPromotion promotion = new ThresholdDiscountPromotion(1000, 100);
        orderService.setThresholdDiscountPromotion(promotion);
        
        Order order = orderService.processOrder(items);
        
        // Then the order summary should be originalAmount: 1600, discount: 100, totalAmount: 1500
        System.out.println("Then: the order summary should be originalAmount: 1600, discount: 100, totalAmount: 1500");
        System.out.println("Actual originalAmount: " + order.getOriginalAmount());
        System.out.println("Actual discount: " + order.getDiscount());
        System.out.println("Actual totalAmount: " + order.getTotalAmount());
        
        // And the customer should receive T-shirt(2), Pants(1)
        System.out.println("And: the customer should receive T-shirt(2), Pants(1)");
        System.out.println("Actual items count: " + order.getItems().size());
        for (OrderItem item : order.getItems()) {
            System.out.println("Actual product: " + item.getProduct().getName() + 
                             ", quantity: " + item.getQuantity());
        }
        
        // Check test result
        boolean testPassed = (order.getOriginalAmount() == 1600) && 
                           (order.getDiscount() == 100) &&
                           (order.getTotalAmount() == 1500) &&
                           (order.getItems().size() == 2);
        
        System.out.println("Test Result: " + (testPassed ? "PASS" : "FAIL"));
        
        if (!testPassed) {
            System.out.println("Expected: originalAmount=1600, discount=100, totalAmount=1500, items=2");
            System.out.println("Actual: originalAmount=" + order.getOriginalAmount() + 
                             ", discount=" + order.getDiscount() +
                             ", totalAmount=" + order.getTotalAmount() +
                             ", items=" + order.getItems().size());
        }
    }
    
    public static void testBuyOneGetOneForCosmetics() {
        System.out.println("\nScenario: Buy-one-get-one for cosmetics - multiple products");
        
        // Given the buy one get one promotion for cosmetics is active
        System.out.println("Given: the buy one get one promotion for cosmetics is active");
        
        // When a customer places an order with Lipstick(cosmetics, 1, 300) and Foundation(cosmetics, 1, 400)
        System.out.println("When: a customer places an order with Lipstick(cosmetics, 1, 300) and Foundation(cosmetics, 1, 400)");
        
        Product lipstick = new Product("Lipstick", "cosmetics", 300);
        Product foundation = new Product("Foundation", "cosmetics", 400);
        OrderItem item1 = new OrderItem(lipstick, 1);
        OrderItem item2 = new OrderItem(foundation, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        
        OrderService orderService = new OrderService();
        // Set buy one get one promotion for cosmetics
        BuyOneGetOnePromotion bogoPromotion = new BuyOneGetOnePromotion("cosmetics");
        orderService.setBuyOneGetOnePromotion(bogoPromotion);
        
        Order order = orderService.processOrder(items);
        
        // Then the order summary should have totalAmount 700
        System.out.println("Then: the order summary should have totalAmount 700");
        System.out.println("Actual totalAmount: " + order.getTotalAmount());
        
        // And the customer should receive Lipstick(2), Foundation(2)
        System.out.println("And: the customer should receive Lipstick(2), Foundation(2)");
        System.out.println("Actual final items count: " + order.getFinalItems().size());
        for (FinalOrderItem item : order.getFinalItems()) {
            System.out.println("Actual product: " + item.getProduct().getName() + 
                             ", quantity: " + item.getQuantity());
        }
        
        // Check test result
        boolean testPassed = (order.getTotalAmount() == 700) &&
                           (order.getFinalItems().size() == 2) &&
                           hasFinalProduct(order, "Lipstick", 2) &&
                           hasFinalProduct(order, "Foundation", 2);
        
        System.out.println("Test Result: " + (testPassed ? "PASS" : "FAIL"));
        
        if (!testPassed) {
            System.out.println("Expected: totalAmount=700, Lipstick(2), Foundation(2)");
        }
    }
    
    public static void testBuyOneGetOneSameProductTwice() {
        System.out.println("\nScenario: Buy-one-get-one for cosmetics - same product twice");
        
        // Given the buy one get one promotion for cosmetics is active
        System.out.println("Given: the buy one get one promotion for cosmetics is active");
        
        // When a customer places an order with Lipstick(cosmetics, 2, 300)
        System.out.println("When: a customer places an order with Lipstick(cosmetics, 2, 300)");
        
        Product lipstick = new Product("Lipstick", "cosmetics", 300);
        OrderItem item1 = new OrderItem(lipstick, 2);
        List<OrderItem> items = new ArrayList<>();
        items.add(item1);
        
        OrderService orderService = new OrderService();
        BuyOneGetOnePromotion bogoPromotion = new BuyOneGetOnePromotion("cosmetics");
        orderService.setBuyOneGetOnePromotion(bogoPromotion);
        
        Order order = orderService.processOrder(items);
        
        // Then the order summary should have totalAmount 600
        System.out.println("Then: the order summary should have totalAmount 600");
        System.out.println("Actual totalAmount: " + order.getTotalAmount());
        
        // And the customer should receive Lipstick(3)
        System.out.println("And: the customer should receive Lipstick(3)");
        System.out.println("Actual final items count: " + order.getFinalItems().size());
        for (FinalOrderItem item : order.getFinalItems()) {
            System.out.println("Actual product: " + item.getProduct().getName() + 
                             ", quantity: " + item.getQuantity());
        }
        
        // Check test result
        boolean testPassed = (order.getTotalAmount() == 600) &&
                           (order.getFinalItems().size() == 1) &&
                           hasFinalProduct(order, "Lipstick", 3);
        
        System.out.println("Test Result: " + (testPassed ? "PASS" : "FAIL"));
        
        if (!testPassed) {
            System.out.println("Expected: totalAmount=600, Lipstick(3)");
        }
    }
    
    public static void testBuyOneGetOneMixedCategories() {
        System.out.println("\nScenario: Buy-one-get-one for cosmetics - mixed categories");
        
        // Given the buy one get one promotion for cosmetics is active
        System.out.println("Given: the buy one get one promotion for cosmetics is active");
        
        // When a customer places an order with Socks(apparel, 1, 100) and Lipstick(cosmetics, 1, 300)
        System.out.println("When: a customer places an order with Socks(apparel, 1, 100) and Lipstick(cosmetics, 1, 300)");
        
        Product socks = new Product("Socks", "apparel", 100);
        Product lipstick = new Product("Lipstick", "cosmetics", 300);
        OrderItem item1 = new OrderItem(socks, 1);
        OrderItem item2 = new OrderItem(lipstick, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        
        OrderService orderService = new OrderService();
        BuyOneGetOnePromotion bogoPromotion = new BuyOneGetOnePromotion("cosmetics");
        orderService.setBuyOneGetOnePromotion(bogoPromotion);
        
        Order order = orderService.processOrder(items);
        
        // Then the order summary should have totalAmount 400
        System.out.println("Then: the order summary should have totalAmount 400");
        System.out.println("Actual totalAmount: " + order.getTotalAmount());
        
        // And the customer should receive Socks(1), Lipstick(2)
        System.out.println("And: the customer should receive Socks(1), Lipstick(2)");
        System.out.println("Actual final items count: " + order.getFinalItems().size());
        for (FinalOrderItem item : order.getFinalItems()) {
            System.out.println("Actual product: " + item.getProduct().getName() + 
                             ", quantity: " + item.getQuantity());
        }
        
        // Check test result
        boolean testPassed = (order.getTotalAmount() == 400) &&
                           (order.getFinalItems().size() == 2) &&
                           hasFinalProduct(order, "Socks", 1) &&
                           hasFinalProduct(order, "Lipstick", 2);
        
        System.out.println("Test Result: " + (testPassed ? "PASS" : "FAIL"));
        
        if (!testPassed) {
            System.out.println("Expected: totalAmount=400, Socks(1), Lipstick(2)");
        }
    }
    
    public static void testMultiplePromotionsStacked() {
        System.out.println("\nScenario: Multiple promotions stacked");
        
        // Given threshold discount and buy one get one promotions are active
        System.out.println("Given: threshold discount (1000->100) and buy one get one for cosmetics are active");
        
        // When a customer places an order with T-shirt(apparel, 3, 500) and Lipstick(cosmetics, 1, 300)
        System.out.println("When: a customer places an order with T-shirt(apparel, 3, 500) and Lipstick(cosmetics, 1, 300)");
        
        Product tshirt = new Product("T-shirt", "apparel", 500);
        Product lipstick = new Product("Lipstick", "cosmetics", 300);
        OrderItem item1 = new OrderItem(tshirt, 3);
        OrderItem item2 = new OrderItem(lipstick, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        
        OrderService orderService = new OrderService();
        ThresholdDiscountPromotion thresholdPromotion = new ThresholdDiscountPromotion(1000, 100);
        BuyOneGetOnePromotion bogoPromotion = new BuyOneGetOnePromotion("cosmetics");
        orderService.setThresholdDiscountPromotion(thresholdPromotion);
        orderService.setBuyOneGetOnePromotion(bogoPromotion);
        
        Order order = orderService.processOrder(items);
        
        // Then the order summary should be originalAmount: 1800, discount: 100, totalAmount: 1700
        System.out.println("Then: the order summary should be originalAmount: 1800, discount: 100, totalAmount: 1700");
        System.out.println("Actual originalAmount: " + order.getOriginalAmount());
        System.out.println("Actual discount: " + order.getDiscount());
        System.out.println("Actual totalAmount: " + order.getTotalAmount());
        
        // And the customer should receive T-shirt(3), Lipstick(2)
        System.out.println("And: the customer should receive T-shirt(3), Lipstick(2)");
        System.out.println("Actual final items count: " + order.getFinalItems().size());
        for (FinalOrderItem item : order.getFinalItems()) {
            System.out.println("Actual product: " + item.getProduct().getName() + 
                             ", quantity: " + item.getQuantity());
        }
        
        // Check test result
        boolean testPassed = (order.getOriginalAmount() == 1800) &&
                           (order.getDiscount() == 100) &&
                           (order.getTotalAmount() == 1700) &&
                           (order.getFinalItems().size() == 2) &&
                           hasFinalProduct(order, "T-shirt", 3) &&
                           hasFinalProduct(order, "Lipstick", 2);
        
        System.out.println("Test Result: " + (testPassed ? "PASS" : "FAIL"));
        
        if (!testPassed) {
            System.out.println("Expected: originalAmount=1800, discount=100, totalAmount=1700, T-shirt(3), Lipstick(2)");
        }
    }
    
    private static boolean hasFinalProduct(Order order, String productName, int expectedQuantity) {
        for (FinalOrderItem item : order.getFinalItems()) {
            if (item.getProduct().getName().equals(productName) && 
                item.getQuantity() == expectedQuantity) {
                return true;
            }
        }
        return false;
    }
}
