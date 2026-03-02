package com.shopsphere.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BaseScreen - Parent for all mobile screen objects.
 * Uses Appium's PageFactory with AppiumFieldDecorator.
 * Mirrors the web BasePage structure for consistency.
 */
public abstract class BaseScreen {
    protected final AppiumDriver driver;
    protected final Logger log;
    private static final int DEFAULT_WAIT = 20;

    public BaseScreen(AppiumDriver driver) {
        this.driver = driver;
        this.log = LogManager.getLogger(getClass());
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(DEFAULT_WAIT)), this);
    }

    protected WebElement waitForVisibility(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void tap(WebElement element) {
        log.debug("Tapping element");
        element.click();
    }

    protected void tap(By locator) {
        waitForVisibility(locator).click();
    }

    protected void typeText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(WebElement element) {
        return element.getText().trim();
    }

    protected boolean isDisplayed(WebElement element) {
        try { return element.isDisplayed(); }
        catch (Exception e) { return false; }
    }

    protected void swipeUp() {
        // Appium gesture — in full impl uses TouchAction or W3C Actions
        log.debug("Swiping up");
    }

    protected void swipeDown() {
        log.debug("Swiping down");
    }

    public abstract boolean isLoaded();
}
