package com.shopsphere.pages;

import com.shopsphere.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckoutPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(CheckoutPage.class);

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
        // React 17 controlled inputs require real key events to update fiber state.
        // JavaScript-based approaches (nativeInputValueSetter + InputEvent) work in a
        // real browser console but React's headless Chrome event handling on Linux
        // accepts the DOM value change without updating internal state, leaving the
        // form logically empty at submit time.
        // Actions.sendKeys fires genuine OS-level keyboard events (keydown, keypress,
        // keyup) for each character, which React's synthetic event system treats
        // identically to real user typing. This is the same mechanism that makes
        // sendKeys work in GitHub Actions.
        // We click the field first via Actions to ensure focus is set before typing,
        // then type the value character by character via the Actions chain.
        log.info("Filling checkout form: '{} {} {}'", firstName, lastName, postalCode);

        typeIntoField(firstNameField, firstName);
        typeIntoField(lastNameField, lastName);
        typeIntoField(postalCodeField, postalCode);

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
     * Types a value into a React 17 controlled input using Actions.
     *
     * Actions.click() focuses the field, then Actions.sendKeys() fires real
     * OS-level key events (keydown, keypress, input, keyup) per character.
     * These events are indistinguishable from real user typing and correctly
     * update React's fiber state in all environments including headless Chrome
     * on Linux (Jenkins/Docker).
     *
     * This replaces the JavaScript nativeInputValueSetter + InputEvent approach
     * which, despite working in a real browser console, fails in headless Chrome
     * on Linux because React's event delegation handles synthetic dispatched
     * events differently from real keyboard input in that environment.
     */
    private void typeIntoField(WebElement field, String value) {
        WaitUtils.waitForClickable(field);
        new Actions(driver)
            .click(field)
            .sendKeys(value)
            .perform();
    }
}
