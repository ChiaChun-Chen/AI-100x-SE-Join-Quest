package com.ecommerce.order;

import io.cucumber.junit.platform.engine.CucumberTestEngine;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class AllFeaturesTest {
    // 統一執行所有 feature 檔案的測試入口點
}
