package com.ecommerce.order;

import io.cucumber.junit.platform.engine.CucumberTestEngine;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("order.feature")
public class OrderPricingCucumberTest {
}
