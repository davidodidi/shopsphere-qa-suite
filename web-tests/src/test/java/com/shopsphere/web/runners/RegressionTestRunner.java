package com.shopsphere.web.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Regression Test Runner — full suite, runs nightly on CI.
 * Parallel execution for speed.
 */
@CucumberOptions(
        features  = "src/test/resources/features",
        glue      = {"com.shopsphere.web.stepdefs", "com.shopsphere.web.hooks"},
        tags      = "@regression",
        plugin    = {
                "pretty",
                "html:target/cucumber-reports/regression-report.html",
                "json:target/cucumber-reports/regression.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class RegressionTestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() { return super.scenarios(); }
}
