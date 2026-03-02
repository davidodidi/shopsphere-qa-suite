package com.shopsphere.mobile.hooks;

import com.shopsphere.config.AppiumDriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobileHooks {
    private static final Logger log = LogManager.getLogger(MobileHooks.class);

    @Before
    public void setUp(Scenario scenario) {
        log.info("=== Mobile Scenario Starting: {} ===", scenario.getName());
        AppiumDriverManager.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            log.warn("Mobile scenario FAILED: {}", scenario.getName());
            try {
                AppiumDriver driver = AppiumDriverManager.getDriver();
                byte[] screenshot = driver.getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "FAILED - " + scenario.getName());
            } catch (Exception e) {
                log.error("Failed to capture mobile screenshot: {}", e.getMessage());
            }
        }
        AppiumDriverManager.quitDriver();
    }
}
