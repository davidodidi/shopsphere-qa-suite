package com.shopsphere.pages;

import com.shopsphere.config.DriverManager;
import com.shopsphere.utils.ScreenshotUtils;
import com.shopsphere.utils.WaitUtils;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final Logger log;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.log = LogManager.getLogger(getClass());
        PageFactory.initElements(driver, this);
    }

    @Step("Navigating to URL: {url}")
    public void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
        WaitUtils.waitForPageLoad();
    }

    protected void click(By locator) {
        log.debug("Clicking element: {}", locator);
        WaitUtils.waitForClickable(locator).click();
    }

    protected void click(WebElement element) {
        log.debug("Clicking WebElement");
        WaitUtils.waitForClickable(element).click();
    }

    protected void type(By locator, String text) {
        log.debug("Typing '{}' into: {}", text, locator);
        WebElement el = WaitUtils.waitForVisibility(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void type(WebElement element, String text) {
        WaitUtils.waitForVisibility(element).clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return WaitUtils.waitForVisibility(locator).getText().trim();
    }

    protected String getText(WebElement element) {
        return WaitUtils.waitForVisibility(element).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected void selectByVisibleText(By locator, String text) {
        new Select(driver.findElement(locator)).selectByVisibleText(text);
    }

    protected void hoverOver(WebElement element) {
        new Actions(driver).moveToElement(element).perform();
    }

    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void takeScreenshot(String name) {
        ScreenshotUtils.attachToAllure(name);
    }

    public abstract boolean isLoaded();
}
