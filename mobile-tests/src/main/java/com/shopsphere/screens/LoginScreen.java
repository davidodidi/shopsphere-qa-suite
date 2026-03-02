package com.shopsphere.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

/**
 * LoginScreen - Mobile screen object for ShopSphere/SauceLabs login.
 * Uses @AndroidFindBy and @iOSXCUITFindBy for cross-platform selectors.
 * Mirrors LoginPage.java structure for web/mobile parity testing.
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

    @Override
    public boolean isLoaded() { return isDisplayed(usernameField); }
}
