package com.shopsphere.pages;

import com.shopsphere.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
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
        // React 17 controlled inputs: element.clear() fires a change event that
        // React uses to reset its internal state, leaving the field empty even
        // after sendKeys(). Replacing clear() with click() + CTRL+A selects all
        // existing text without triggering React's reset, then sendKeys() types
        // the new value and React's onChange fires correctly with the full string.
        typeReact(firstNameField, firstName);
        typeReact(lastNameField, lastName);
        typeReact(postalCodeField, postalCode);
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
     * Types into a React 17 controlled input without using clear().
     *
     * element.clear() fires a DOM change event that React intercepts and uses
     * to reset its internal fiber state to empty string — so any subsequent
     * sendKeys() call types into a field React considers already-reset, and the
     * value never propagates correctly to React's state.
     *
     * Instead: click to focus, CTRL+A to select all existing text (no React
     * reset event), then sendKeys(value) replaces the selection. React's
     * onChange fires once with the complete new value and updates fiber state
     * correctly. This works in all environments including headless Chrome in a
     * container with no display server, because sendKeys() goes through the
     * WebDriver wire protocol directly.
     */
    private void typeReact(WebElement field, String value) {
        WaitUtils.waitForClickable(field).click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
    }
}
