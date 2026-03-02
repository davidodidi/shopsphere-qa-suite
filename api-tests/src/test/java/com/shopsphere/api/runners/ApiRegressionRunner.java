package com.shopsphere.api.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.shopsphere.api.stepdefs"},
        tags = "@api",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/api-regression-report.html",
                "json:target/cucumber-reports/api-regression.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)
public class ApiRegressionRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() { return super.scenarios(); }
}
