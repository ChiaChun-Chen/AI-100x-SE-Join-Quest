package com.ecommerce.order.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.datatable.DataTable;

public class OrderStepDefinitions {

    @Given("no promotions are applied")
    public void noPromotionsAreApplied() {
        // TODO: implement step
    }

    @When("a customer places an order with:")
    public void aCustomerPlacesAnOrderWith(DataTable dataTable) {
        // TODO: implement step
    }

    @Then("the order summary should be:")
    public void theOrderSummaryShouldBe(DataTable dataTable) {
        // TODO: implement step
    }

    @And("the customer should receive:")
    public void theCustomerShouldReceive(DataTable dataTable) {
        // TODO: implement step
    }

    @Given("the threshold discount promotion is configured:")
    public void theThresholdDiscountPromotionIsConfigured(DataTable dataTable) {
        // TODO: implement step
    }

    @Given("the buy one get one promotion for cosmetics is active")
    public void theBuyOneGetOnePromotionForCosmeticsIsActive() {
        // TODO: implement step
    }
}
