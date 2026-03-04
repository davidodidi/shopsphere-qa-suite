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
        // Wait for the first name field to be fully clickable before setting values.
        // waitForUrlToContain("checkout-step-one") in CartPage confirms the HTML has
        // arrived, but React mounts its fiber and attaches event handlers asynchronously
        // after the DOM is painted. Under load (e.g. after 20+ sequential scenarios),
        // this mount can take longer than usual. waitForClickable blocks until the
        // element is both visible and enabled, which is the closest proxy for
        // "React has finished mounting controlled inputs on this form".
        // Without this wait, reactSetValue fires before React's fiber is ready and
        // the InputEvent is dropped, leaving fields empty at submit time.
        WaitUtils.waitForClickable(firstNameField);
        reactSetValue(firstNameField, firstName);
        reactSetValue(lastNameField, lastName);
        reactSetValue(postalCodeField, postalCode);
        return this;
    }

    @Step("Clicking continue")
    public CheckoutPage clickContinue() {
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
     * Sets a value on a React 17 controlled input.
     *
     * React 17 tracks input state via its fiber reconciler and ignores direct
     * DOM property writes and plain Event('input') dispatches.
     * A native InputEvent with inputType='insertText' and data set matches what
     * Chrome fires on real keyboard input, hooking into React's synthetic event
     * system and updating fiber state so the value persists at submit time.
     *
     * Verified against SauceDemo checkout page in Chrome 145 console:
     * all three fields populated and form navigated to step-two on click.
     */
    private void reactSetValue(WebElement field, String value) {
        WaitUtils.waitForVisibility(field);
        ((JavascriptExecutor) driver).executeScript(
            "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(" +
            "    window.HTMLInputElement.prototype, 'value').set;" +
            "nativeInputValueSetter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new InputEvent('input', {" +
            "    bubbles: true," +
            "    inputType: 'insertText'," +
            "    data: arguments[1]" +
            "}));",
            field, value
        );
    }
}
