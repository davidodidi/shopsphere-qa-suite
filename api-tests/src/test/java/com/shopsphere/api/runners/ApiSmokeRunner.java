package com.shopsphere.api.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.shopsphere.api.stepdefs"},
        tags = "@smoke and @api",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/api-smoke-report.html",
                "json:target/cucumber-reports/api-smoke.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)
public class ApiSmokeRunner extends AbstractTestNGCucumberTests {}
