package com.shopsphere.web.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Sanity Test Runner — post-deployment verification of key functionality.
 * Run after each deployment to catch critical regressions.
 */
@CucumberOptions(
        features  = "src/test/resources/features",
        glue      = {"com.shopsphere.web.stepdefs", "com.shopsphere.web.hooks"},
        tags      = "@sanity",
        plugin    = {
                "pretty",
                "html:target/cucumber-reports/sanity-report.html",
                "json:target/cucumber-reports/sanity.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class SanityTestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() { return super.scenarios(); }
}
