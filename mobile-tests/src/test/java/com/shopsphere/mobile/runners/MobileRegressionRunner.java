package com.shopsphere.mobile.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.shopsphere.mobile.stepdefs", "com.shopsphere.mobile.hooks"},
        tags = "@mobile",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/mobile-regression-report.html",
                "json:target/cucumber-reports/mobile-regression.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)
public class MobileRegressionRunner extends AbstractTestNGCucumberTests {}
