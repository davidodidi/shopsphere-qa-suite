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
        // SauceDemo runs React 17. React 17 changed how it tracks controlled input state.
        // Setting element.value via nativeInputValueSetter + plain Event('input') works
        // for React 16 but is ignored by React 17's fiber reconciler.
        // React 17 requires a native InputEvent with inputType and data properties set,
        // which matches what the browser fires on real keyboard input.
        // Verified in browser console: this approach navigates to step-two on click.
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
     * DOM property writes. It also ignores plain Event('input') dispatches.
     * What React 17 DOES recognise is a native InputEvent with inputType and
     * data set — this matches what Chrome fires on real keyboard input and hooks
     * into React's synthetic event system, updating the fiber state so the value
     * is present when the form submits.
     *
     * Verified against SauceDemo checkout page in Chrome 145 console:
     * nativeInputValueSetter + InputEvent('input', inputType='insertText', data=value)
     * successfully populates all three fields and allows form submission to step-two.
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
