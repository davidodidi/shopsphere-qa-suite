package com.shopsphere.web.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * E2E Test Runner — full end-to-end business flows.
 * Covers complete user journeys across multiple pages.
 */
@CucumberOptions(
        features  = "src/test/resources/features",
        glue      = {"com.shopsphere.web.stepdefs", "com.shopsphere.web.hooks"},
        tags      = "@e2e",
        plugin    = {
                "pretty",
                "html:target/cucumber-reports/e2e-report.html",
                "json:target/cucumber-reports/e2e.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class E2ETestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() { return super.scenarios(); }
}
