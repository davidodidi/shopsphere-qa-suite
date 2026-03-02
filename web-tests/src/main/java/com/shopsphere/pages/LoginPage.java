package com.shopsphere.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Page Object using Page Factory (@FindBy annotations).
 * Targets SauceDemo login page (https://www.saucedemo.com).
 */
public class LoginPage extends BasePage {

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(css = ".login_logo")
    private WebElement loginLogo;

    public LoginPage() {
        super();
    }

    @Step("Entering username: {username}")
    public LoginPage enterUsername(String username) {
        log.info("Entering username: {}", username);
        type(usernameField, username);
        return this;
    }

    @Step("Entering password")
    public LoginPage enterPassword(String password) {
        log.info("Entering password");
        type(passwordField, password);
        return this;
    }

    @Step("Clicking login button")
    public ProductsPage clickLogin() {
        log.info("Clicking login button");
        click(loginButton);
        return new ProductsPage();
    }

    @Step("Attempting login with invalid credentials")
    public LoginPage clickLoginExpectingFailure() {
        click(loginButton);
        return this;
    }

    @Step("Logging in as user: {username}")
    public ProductsPage loginAs(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isLoaded() {
        return isDisplayed(loginLogo);
    }
}
