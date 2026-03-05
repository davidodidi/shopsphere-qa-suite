package com.shopsphere.screens;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BaseScreen {

    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected final Logger log = LogManager.getLogger(getClass());

    public BaseScreen(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    protected void typeText(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    /**
     * Alias for click() — used by screen classes that call tap().
     */
    protected void tap(WebElement element) {
        click(element);
    }

    protected boolean isVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the element is present in the DOM and visible.
     * Does NOT throw — safe to use in isLoaded() checks.
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected String getText(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    /**
     * Scrolls down by swiping from 75% to 25% of the screen height.
     * Used by CartScreen and CheckoutScreen to reveal below-the-fold elements.
     */
    protected void scrollDown() {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.75);
            int endY   = (int) (size.height * 0.25);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);

            swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(
                PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(600),
                PointerInput.Origin.viewport(), startX, endY));
            swipe.addAction(finger.createPointerUp(
                PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(List.of(swipe));
        } catch (Exception e) {
            log.warn("scrollDown() failed: {}", e.getMessage());
        }
    }

    /**
     * Subclasses must implement this to verify the screen has fully loaded.
     */
    public abstract boolean isLoaded();
}
