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
        // SauceDemo is a React app with controlled inputs.
        // Setting element.value directly via JS is silently overwritten by React
        // on the next render because React tracks its own internal fiber state.
        // Dispatching a plain Event('input') is also ignored for the same reason.
        //
        // The correct approach for React controlled inputs is to use the native
        // input value setter from Object.getOwnPropertyDescriptor, then dispatch
        // a native InputEvent. This hooks into React's synthetic event system
        // and updates React's internal state, so the value persists at submit time.
        reactSetValue(firstNameField, firstName);
        reactSetValue(lastNameField, lastName);
        reactSetValue(postalCodeField, postalCode);
        return this;
    }

    @Step("Clicking continue")
    public CheckoutPage clickContinue() {
        // Real WebDriver click on the submit input triggers form submission.
        // The form has no action/method so it does a GET to the current URL
        // with field values as query params, then React router navigates to step-two.
        // We must ensure fields have values (via reactSetValue above) before clicking.
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
     * Sets a value on a React controlled input by bypassing React's synthetic
     * event system correctly.
     *
     * React controlled inputs maintain their own internal fiber state. Setting
     * element.value directly via JS is overwritten by React on the next render.
     * Dispatching a plain Event('input') is not recognised by React's synthetic
     * system. The correct fix is to use the native HTMLInputElement value setter
     * (obtained via Object.getOwnPropertyDescriptor on the prototype) and then
     * dispatch a native InputEvent, which React's internals do recognise.
     */
    private void reactSetValue(WebElement field, String value) {
        WaitUtils.waitForVisibility(field);
        ((JavascriptExecutor) driver).executeScript(
            "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(" +
            "    window.HTMLInputElement.prototype, 'value').set;" +
            "nativeInputValueSetter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
            field, value
        );
    }
}
