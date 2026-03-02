package com.shopsphere.mobile.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Mobile Smoke Test Runner.
 * Runs same @smoke scenarios as web, but via Appium/mobile step definitions.
 * Demonstrates cross-platform testing from a single codebase.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.shopsphere.mobile.stepdefs", "com.shopsphere.mobile.hooks"},
        tags = "@smoke and @mobile",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/mobile-smoke-report.html",
                "json:target/cucumber-reports/mobile-smoke.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)
public class MobileSmokeRunner extends AbstractTestNGCucumberTests {}
