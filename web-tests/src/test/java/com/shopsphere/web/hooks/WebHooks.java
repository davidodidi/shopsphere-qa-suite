package com.shopsphere.web.hooks;

import com.shopsphere.config.ConfigReader;
import com.shopsphere.config.DriverManager;
import com.shopsphere.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cucumber Hooks for Web tests.
 * Manages driver lifecycle, screenshots on failure, and Allure integration.
 */
public class WebHooks {
    private static final Logger log = LogManager.getLogger(WebHooks.class);

    @Before
    public void setUp(Scenario scenario) {
        log.info("=== Starting Scenario: {} [Tags: {}] ===", scenario.getName(), scenario.getSourceTagNames());
        DriverManager.initDriver();
        Allure.getLifecycle().updateTestCase(tc -> tc.setName(scenario.getName()));
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            log.warn("Scenario FAILED: {} — capturing screenshot", scenario.getName());
            ScreenshotUtils.attachToAllure("FAILED - " + scenario.getName());
        } else {
            log.info("Scenario PASSED: {}", scenario.getName());
        }
        DriverManager.quitDriver();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            ScreenshotUtils.attachToAllure("Step failed in: " + scenario.getName());
        }
    }
}
