package com.shopsphere.web.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * UAT Test Runner — business stakeholder acceptance scenarios.
 * These scenarios are written in plain English for non-technical sign-off.
 */
@CucumberOptions(
        features  = "src/test/resources/features/uat.feature",
        glue      = {"com.shopsphere.web.stepdefs", "com.shopsphere.web.hooks"},
        tags      = "@uat",
        plugin    = {
                "pretty",
                "html:target/cucumber-reports/uat-report.html",
                "json:target/cucumber-reports/uat.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class UATTestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() { return super.scenarios(); }
}
