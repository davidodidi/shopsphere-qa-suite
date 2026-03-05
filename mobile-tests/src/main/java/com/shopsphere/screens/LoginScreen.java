package com.shopsphere.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

/**
 * LoginScreen - Mobile screen object for ShopSphere/SauceLabs login.
 * Uses @AndroidFindBy and @iOSXCUITFindBy for cross-platform selectors.
 * Mirrors LoginPage.java structure for web/mobile parity testing.
 *
 * FIX: PageFactory.initElements() with AppiumFieldDecorator is now called in
 * the constructor — without this, @AndroidFindBy/@iOSXCUITFindBy annotations
 * are never processed, leaving all WebElement fields as null and causing
 * isLoaded() to always return false.
 */
public class LoginScreen extends BaseScreen {

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(accessibility = "test-Username")
    private WebElement usernameField;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(accessibility = "test-Password")
    private WebElement passwordField;

    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(accessibility = "test-LOGIN")
    private WebElement loginButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Error message']/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='test-Error message']")
    private WebElement errorMessage;

    public LoginScreen(AppiumDriver driver) {
        super(driver);
        // CRITICAL FIX: initialises all @AndroidFindBy / @iOSXCUITFindBy annotated fields.
        // Without this call every WebElement field stays null and isLoaded() always returns false.
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(15)), this);
    }

    @Step("Entering username: {username}")
    public LoginScreen enterUsername(String username) {
        log.info("Entering mobile username: {}", username);
        typeText(usernameField, username);
        return this;
    }

    @Step("Entering password")
    public LoginScreen enterPassword(String password) {
        typeText(passwordField, password);
        return this;
    }

    @Step("Tapping login button")
    public ProductsScreen tapLogin() {
        tap(loginButton);
        return new ProductsScreen(driver);
    }

    @Step("Tapping login expecting failure")
    public LoginScreen tapLoginExpectingFailure() {
        tap(loginButton);
        return this;
    }

    @Step("Login as {username}")
    public ProductsScreen loginAs(String username, String password) {
        return enterUsername(username).enterPassword(password).tapLogin();
    }

    public String getErrorMessage() { return getText(errorMessage); }
    public boolean isErrorDisplayed() { return isDisplayed(errorMessage); }

    /**
     * The screen is considered loaded when the username field is visible.
     * AppiumFieldDecorator already applies a timeout during initElements,
     * so this check is a lightweight presence assertion.
     */
    @Override
    public boolean isLoaded() {
        return isDisplayed(usernameField);
    }
}
