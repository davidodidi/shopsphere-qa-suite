package com.shopsphere.pages;

import com.shopsphere.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(css = ".complete-header")
    private WebElement orderConfirmationHeader;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public CheckoutPage() { super(); }

    @Step("Filling checkout info: {firstName} {lastName} {postalCode}")
    public CheckoutPage fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        // sendKeys after clear() can fail to commit values in headless Chrome under
        // parallel load — the proxy reference can go stale between clear() and sendKeys().
        // Setting value via JS is atomic and guaranteed to reach the DOM.
        jsSetValue(firstNameField, firstName);
        jsSetValue(lastNameField, lastName);
        jsSetValue(postalCodeField, postalCode);
        return this;
    }

    @Step("Clicking continue")
    public CheckoutPage clickContinue() {
        // Wait for the continue button to confirm we are on step-one,
        // then do a real WebDriver click — this triggers both the click event
        // and form validation in the browser's native event loop.
        // form.submit() bypasses validation and submits before JS sets values.
        // jsClick() fires a DOM event but doesn't trigger form submission in headless.
        // A real waitForClickable().click() is correct here because the fields
        // are already populated via jsSetValue(), so there is nothing to race.
        WaitUtils.waitForClickable(continueButton).click();
        WaitUtils.waitForUrlToContain("checkout-step-two");
        return this;
    }

    @Step("Clicking continue expecting validation error")
    public CheckoutPage clickContinueExpectingError() {
        WaitUtils.waitForClickable(continueButton).click();
        return this;
    }

    @Step("Finishing order")
    public CheckoutPage finishOrder() {
        scrollToElement(finishButton);
        jsClick(finishButton);
        WaitUtils.waitForUrlToContain("checkout-complete");
        return this;
    }

    public String getOrderConfirmation() { return getText(orderConfirmationHeader); }
    public String getErrorMessage()      { return getText(errorMessage); }

    public boolean isOrderComplete() {
        try {
            return orderConfirmationHeader.isDisplayed()
                    && getOrderConfirmation().toLowerCase().contains("thank you");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("checkout");
    }

    /**
     * Sets an input field's value via JavaScript and dispatches an 'input' event
     * so the page's own JS (React, etc.) registers the change.
     * This is more reliable than sendKeys in headless CI because it is atomic —
     * there is no gap between clear() and sendKeys() where the proxy can go stale.
     */
    private void jsSetValue(WebElement field, String value) {
        WaitUtils.waitForVisibility(field);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = arguments[1];" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            field, value
        );
    }
}
