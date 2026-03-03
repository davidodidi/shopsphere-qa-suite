package com.shopsphere.screens;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

/**
 * BaseScreen - Parent for all mobile screen objects.
 * Shared utilities: waits, taps, text input, scrolling.
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

    /**
     * Scrolls down by swiping finger from 80% to 20% of screen height.
     * Reusable across all screens — avoids duplication in CartScreen, CheckoutScreen etc.
     */
    protected void scrollDown() {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.8);
            int endY   = (int) (size.height * 0.2);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(List.of(swipe));
            log.info("Scrolled down on {}", getClass().getSimpleName());
        } catch (Exception e) {
            log.warn("Scroll failed on {}: {}", getClass().getSimpleName(), e.getMessage());
        }
    }

    protected void swipeUp() {
        log.debug("Swiping up");
    }

    protected void swipeDown() {
        log.debug("Swiping down");
    }

    public abstract boolean isLoaded();
}
