package com.shopsphere.utils;

import com.shopsphere.config.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WaitUtils {

    private static final Logger log = LogManager.getLogger(WaitUtils.class);
    private static final int DEFAULT_WAIT = 20;

    private WaitUtils() {}

    public static WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, DEFAULT_WAIT);
    }

    public static WebElement waitForVisibility(By locator, int seconds) {
        WebDriver driver = DriverManager.getDriver();
        log.debug("Waiting {}s for element to be visible: {}", seconds, locator);
        return new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(WebElement element) {
        return waitForVisibility(element, DEFAULT_WAIT);
    }

    public static WebElement waitForVisibility(WebElement element, int seconds) {
        log.debug("Waiting {}s for WebElement to be visible", seconds);
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForClickable(By locator) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(DEFAULT_WAIT))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(WebElement element) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(DEFAULT_WAIT))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public static boolean waitForInvisibility(By locator) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(DEFAULT_WAIT))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForPageLoad() {
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(30))
                .until(driver -> ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    public static void waitForUrlToContain(String fragment) {
        log.debug("Waiting for URL to contain: {}", fragment);
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(DEFAULT_WAIT))
                .until(ExpectedConditions.urlContains(fragment));
    }
}
