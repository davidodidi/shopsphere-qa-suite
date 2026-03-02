package com.shopsphere.web.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Smoke Test Runner — fast confidence checks on critical paths.
 * Runs on every PR and deploy. Target: < 5 minutes.
 */
@CucumberOptions(
        features  = "src/test/resources/features",
        glue      = {"com.shopsphere.web.stepdefs", "com.shopsphere.web.hooks"},
        tags      = "@smoke",
        plugin    = {
                "pretty",
                "html:target/cucumber-reports/smoke-report.html",
                "json:target/cucumber-reports/smoke.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class SmokeTestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
