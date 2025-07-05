package com.ecommerce.order.steps;

import com.ecommerce.order.model.*;
import com.ecommerce.order.service.OrderService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class OrderStepDefinitions {
    
    private OrderService orderService;
    private Order processedOrder;
    private List<OrderItem> orderItems;
    
    public OrderStepDefinitions() {
        this.orderService = new OrderService();
        this.orderItems = new ArrayList<>();
    }

    @Given("no promotions are applied")
    public void noPromotionsAreApplied() {
        // Reset order service - no promotions configured
        this.orderService = new OrderService();
    }

    @When("a customer places an order with:")
    public void aCustomerPlacesAnOrderWith(DataTable dataTable) {
        this.orderItems = new ArrayList<>();
        
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String productName = row.get("productName");
            int quantity = Integer.parseInt(row.get("quantity"));
            int unitPrice = Integer.parseInt(row.get("unitPrice"));
            String category = row.getOrDefault("category", "general");
            
            Product product = new Product(productName, category, unitPrice);
            OrderItem item = new OrderItem(product, quantity);
            orderItems.add(item);
        }
        
        processedOrder = orderService.processOrder(orderItems);
    }

    @Then("the order summary should be:")
    public void theOrderSummaryShouldBe(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> expectedSummary = rows.get(0);
        
        if (expectedSummary.containsKey("totalAmount")) {
            int expectedTotal = Integer.parseInt(expectedSummary.get("totalAmount"));
            assertEquals(expectedTotal, processedOrder.getTotalAmount(), 
                "Total amount should match expected value");
        }
        
        if (expectedSummary.containsKey("originalAmount")) {
            int expectedOriginal = Integer.parseInt(expectedSummary.get("originalAmount"));
            assertEquals(expectedOriginal, processedOrder.getOriginalAmount(), 
                "Original amount should match expected value");
        }
        
        if (expectedSummary.containsKey("discount")) {
            int expectedDiscount = Integer.parseInt(expectedSummary.get("discount"));
            assertEquals(expectedDiscount, processedOrder.getDiscount(), 
                "Discount should match expected value");
        }
    }

    @And("the customer should receive:")
    public void theCustomerShouldReceive(DataTable dataTable) {
        List<Map<String, String>> expectedItems = dataTable.asMaps(String.class, String.class);
        
        // Check if we have final items (with promotions) or original items
        List<FinalOrderItem> finalItems = processedOrder.getFinalItems();
        if (finalItems.isEmpty()) {
            // Use original items if no final items (no promotions applied)
            List<OrderItem> originalItems = processedOrder.getItems();
            assertEquals(expectedItems.size(), originalItems.size(), 
                "Number of items should match expected");
                
            for (Map<String, String> expectedItem : expectedItems) {
                String expectedProductName = expectedItem.get("productName");
                int expectedQuantity = Integer.parseInt(expectedItem.get("quantity"));
                
                boolean found = originalItems.stream().anyMatch(item -> 
                    item.getProduct().getName().equals(expectedProductName) && 
                    item.getQuantity() == expectedQuantity);
                    
                assertTrue(found, "Should find item: " + expectedProductName + " with quantity: " + expectedQuantity);
            }
        } else {
            // Use final items (with promotions applied)
            assertEquals(expectedItems.size(), finalItems.size(), 
                "Number of final items should match expected");
                
            for (Map<String, String> expectedItem : expectedItems) {
                String expectedProductName = expectedItem.get("productName");
                int expectedQuantity = Integer.parseInt(expectedItem.get("quantity"));
                
                boolean found = finalItems.stream().anyMatch(item -> 
                    item.getProduct().getName().equals(expectedProductName) && 
                    item.getQuantity() == expectedQuantity);
                    
                assertTrue(found, "Should find final item: " + expectedProductName + " with quantity: " + expectedQuantity);
            }
        }
    }

    @Given("the threshold discount promotion is configured:")
    public void theThresholdDiscountPromotionIsConfigured(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> promotionConfig = rows.get(0);
        
        int threshold = Integer.parseInt(promotionConfig.get("threshold"));
        int discount = Integer.parseInt(promotionConfig.get("discount"));
        
        ThresholdDiscountPromotion promotion = new ThresholdDiscountPromotion(threshold, discount);
        orderService.setThresholdDiscountPromotion(promotion);
    }

    @Given("the buy one get one promotion for cosmetics is active")
    public void theBuyOneGetOnePromotionForCosmeticsIsActive() {
        BuyOneGetOnePromotion promotion = new BuyOneGetOnePromotion("cosmetics");
        orderService.setBuyOneGetOnePromotion(promotion);
    }
}
